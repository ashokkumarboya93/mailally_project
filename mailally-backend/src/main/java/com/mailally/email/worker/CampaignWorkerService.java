package com.mailally.email.worker;

import com.mailally.campaign.entity.Campaign;
import com.mailally.campaign.repository.CampaignRepository;
import com.mailally.email.cache.RedisProgressCache;
import com.mailally.email.entity.CampaignBatch;
import com.mailally.email.entity.CampaignRecipientLog;
import com.mailally.email.provider.BatchSendResult;
import com.mailally.email.provider.EmailProvider;
import com.mailally.email.provider.EmailProviderFactory;
import com.mailally.email.provider.RecipientBatchItem;
import com.mailally.email.renderer.TemplateRenderer;
import com.mailally.email.repository.CampaignBatchRepository;
import com.mailally.email.repository.CampaignRecipientLogRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * High-Speed Worker service executing campaign batches concurrently using Brevo Batch API and Redis Streams.
 */
@Service
public class CampaignWorkerService {

    private static final Logger log = LoggerFactory.getLogger(CampaignWorkerService.class);
    private static final String STREAM_KEY = "campaign:queue:pending";
    private static final String GROUP_NAME = "campaign-workers-group";
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private final String nodeId = "worker-node-" + UUID.randomUUID().toString().substring(0, 8);

    @Value("${mail.campaign.worker.concurrency:4}")
    private int workerConcurrency;

    @Value("${mailally.email.max-retries:3}")
    private int maxRetries;

    private final StringRedisTemplate redisTemplate;
    private final CampaignBatchRepository batchRepository;
    private final CampaignRecipientLogRepository recipientRepository;
    private final CampaignRepository campaignRepository;
    private final EmailProviderFactory providerFactory;
    private final TemplateRenderer templateRenderer;
    private final RedisProgressCache progressCache;
    private final com.mailally.email.provider.ProviderCircuitBreaker circuitBreaker;
    private ExecutorService workerExecutor;

    public CampaignWorkerService(StringRedisTemplate redisTemplate,
                                 CampaignBatchRepository batchRepository,
                                 CampaignRecipientLogRepository recipientRepository,
                                 CampaignRepository campaignRepository,
                                 EmailProviderFactory providerFactory,
                                 TemplateRenderer templateRenderer,
                                 RedisProgressCache progressCache,
                                 com.mailally.email.provider.ProviderCircuitBreaker circuitBreaker) {
        this.redisTemplate = redisTemplate;
        this.batchRepository = batchRepository;
        this.recipientRepository = recipientRepository;
        this.campaignRepository = campaignRepository;
        this.providerFactory = providerFactory;
        this.templateRenderer = templateRenderer;
        this.progressCache = progressCache;
        this.circuitBreaker = circuitBreaker;
    }

    @PostConstruct
    public void initGroup() {
        this.workerExecutor = Executors.newFixedThreadPool(Math.max(2, workerConcurrency));
        try {
            redisTemplate.opsForStream().createGroup(STREAM_KEY, ReadOffset.from("0-0"), GROUP_NAME);
            log.info("CampaignWorkerService [{}]: Initialized with concurrency={}, group='{}'.", nodeId, workerConcurrency, GROUP_NAME);
        } catch (Exception e) {
            log.debug("Consumer group initialization skipped: {}", e.getMessage());
        }
    }

    @Scheduled(fixedDelay = 250)
    public void pollQueue() {
        try {
            List<MapRecord<String, Object, Object>> records = redisTemplate.opsForStream().read(
                org.springframework.data.redis.connection.stream.Consumer.from(GROUP_NAME, nodeId),
                StreamReadOptions.empty().count(Math.max(1, workerConcurrency)),
                StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed())
            );

            if (records == null || records.isEmpty()) {
                return;
            }

            for (MapRecord<String, Object, Object> record : records) {
                String campaignIdStr = record.getValue().get("campaignId") != null ? record.getValue().get("campaignId").toString() : null;
                String batchIdStr = record.getValue().get("batchId") != null ? record.getValue().get("batchId").toString() : null;
                String providerName = record.getValue().get("provider") != null ? record.getValue().get("provider").toString() : null;

                if (campaignIdStr != null && batchIdStr != null) {
                    Long campaignId = Long.parseLong(campaignIdStr);
                    Long batchId = Long.parseLong(batchIdStr);

                    redisTemplate.opsForStream().acknowledge(STREAM_KEY, GROUP_NAME, record.getId());
                    redisTemplate.opsForStream().delete(STREAM_KEY, record.getId());

                    workerExecutor.submit(() -> processBatch(campaignId, batchId, providerName));
                }
            }
        } catch (Exception e) {
            log.warn("CampaignWorkerService: Error polling stream: {}", e.getMessage());
        }
    }

    private void processBatch(Long campaignId, Long batchId, String providerName) {
        log.info("CampaignWorkerService [{}]: Processing batchId={} for campaignId={}", nodeId, batchId, campaignId);

        CampaignBatch batch = batchRepository.findById(batchId).orElse(null);
        if (batch == null) {
            log.error("Batch not found in DB: {}", batchId);
            return;
        }

        // Idempotency check: Skip if batch is already completed or accepted
        if ("COMPLETED".equals(batch.getStatus()) || "ACCEPTED".equals(batch.getStatus())) {
            log.info("Batch {} already completed/accepted. Skipping.", batchId);
            return;
        }

        batch.setStatus("PROCESSING");
        batch.setWorkerNodeId(nodeId);
        if (batch.getStartedAt() == null) {
            batch.setStartedAt(LocalDateTime.now());
        }
        if (batch.getIdempotencyKey() == null) {
            batch.setIdempotencyKey(UUID.randomUUID().toString());
        }
        batchRepository.save(batch);

        Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
        if (campaign == null) {
            log.error("Campaign not found for batchId={}", batchId);
            batch.setStatus("FAILED");
            batchRepository.save(batch);
            return;
        }

        List<CampaignRecipientLog> allQueued = recipientRepository.findByCampaignIdAndStatus(campaignId, "QUEUED");
        if (allQueued.isEmpty()) {
            batch.setStatus("COMPLETED");
            batch.setCompletedAt(LocalDateTime.now());
            batchRepository.save(batch);
            return;
        }

        int chunkSize = batch.getOptimalSize() != null ? batch.getOptimalSize() : 100;
        List<CampaignRecipientLog> recipients = allQueued.subList(0, Math.min(allQueued.size(), chunkSize));

        List<RecipientBatchItem> validBatchItems = new ArrayList<>();
        List<CampaignRecipientLog> invalidRecipients = new ArrayList<>();

        String fromName = campaign.getFromName() != null ? campaign.getFromName() : "MailAlly";
        String fromEmail = campaign.getSenderEmail() != null ? campaign.getSenderEmail() : "info@marcamor.com";
        String defaultSubject = campaign.getSubject() != null ? campaign.getSubject() : (campaign.getTemplate() != null ? campaign.getTemplate().getSubject() : "MailAlly Campaign");
        String defaultHtmlBody = campaign.getTemplate() != null ? campaign.getTemplate().getHtmlContent() : "<p>MailAlly</p>";

        // Pre-Send Recipient Validation & Personalized Tag Rendering
        for (CampaignRecipientLog r : recipients) {
            r.setWorkerThreadId(Thread.currentThread().getName());
            r.setAttempts(r.getAttempts() + 1);

            if (r.getEmail() == null || !EMAIL_PATTERN.matcher(r.getEmail().trim()).matches()) {
                r.setStatus("INVALID");
                r.setLastError("Pre-send validation failed: Invalid email syntax");
                invalidRecipients.add(r);
                progressCache.incrementFailed(campaignId);
            } else {
                r.setStatus("PROCESSING");

                Map<String, String> params = new HashMap<>();
                if (r.getContact() != null) {
                    params.put("firstName", r.getContact().getFirstName() != null ? r.getContact().getFirstName() : "");
                    params.put("lastName", r.getContact().getLastName() != null ? r.getContact().getLastName() : "");
                    params.put("company", r.getContact().getCompany() != null ? r.getContact().getCompany() : "");
                    params.put("city", r.getContact().getCity() != null ? r.getContact().getCity() : "");
                }

                String personalizedSubject = templateRenderer.render(defaultSubject, r.getContact());
                String personalizedHtml = templateRenderer.render(defaultHtmlBody, r.getContact());

                validBatchItems.add(new RecipientBatchItem(
                        r.getId(),
                        r.getEmail().trim(),
                        r.getContact() != null ? r.getContact().getFirstName() : "",
                        r.getContact() != null ? r.getContact().getLastName() : "",
                        params,
                        personalizedSubject,
                        personalizedHtml
                ));
            }
        }

        // Persist invalid recipient logs immediately
        if (!invalidRecipients.isEmpty()) {
            recipientRepository.saveAll(invalidRecipients);
        }

        if (validBatchItems.isEmpty()) {
            batch.setStatus("COMPLETED");
            batch.setCompletedAt(LocalDateTime.now());
            batchRepository.save(batch);
            return;
        }

        if (!circuitBreaker.allowRequest(providerName)) {
            log.warn("CampaignWorkerService [{}]: Circuit breaker OPEN for provider '{}'. Pausing batchId={}.", nodeId, providerName, batchId);
            batch.setStatus("RETRYING");
            batchRepository.save(batch);
            return;
        }

        EmailProvider provider = providerFactory.getProvider(providerName);
        long startTime = System.currentTimeMillis();

        // Single Provider Batch API Dispatch (Brevo messageVersions)
        BatchSendResult result = provider.sendBatch(
                validBatchItems,
                fromEmail,
                fromName,
                campaign.getReplyTo(),
                defaultSubject,
                defaultHtmlBody,
                batch.getIdempotencyKey()
        );

        long duration = System.currentTimeMillis() - startTime;

        if (result.isSuccess()) {
            circuitBreaker.recordSuccess(providerName);
            Map<Long, String> msgMap = result.getRecipientMessageIds();
            List<CampaignRecipientLog> updatedValid = new ArrayList<>();

            for (CampaignRecipientLog r : recipients) {
                if ("INVALID".equals(r.getStatus())) continue;

                r.setDurationMs((int) duration);
                r.setProviderMessageId(msgMap.get(r.getId()));
                r.setSmtpResponseCode(result.getSmtpResponseCode() != null ? result.getSmtpResponseCode() : "250 OK");
                
                // Enforce Atomic State Transition (QUEUED/PROCESSING -> ACCEPTED)
                r.setStatus("ACCEPTED");
                updatedValid.add(r);

                progressCache.incrementSent(campaignId);
            }

            // Bulk Save Recipients via JDBC Batching
            recipientRepository.saveAll(updatedValid);

            batch.setStatus("COMPLETED");
            batch.setProviderBatchId(result.getProviderBatchId());
            batch.setCompletedAt(LocalDateTime.now());
            batchRepository.save(batch);

            log.info("CampaignWorkerService [{}]: Successfully completed Batch ID={} [Valid: {}, Invalid: {}, Duration: {}ms]",
                    nodeId, batchId, validBatchItems.size(), invalidRecipients.size(), duration);

        } else {
            // Distinguish 429 rate-limit from other failures for circuit breaker
            String responseCode = result.getSmtpResponseCode();
            if ("429".equals(responseCode)) {
                circuitBreaker.recordRateLimit(providerName, result.getRetryAfterSeconds());
            } else {
                circuitBreaker.recordFailure(providerName);
            }

            int currentRetry = batch.getRetryCount() != null ? batch.getRetryCount() + 1 : 1;
            batch.setRetryCount(currentRetry);

            boolean isRetryable = "429".equals(responseCode) || "500".equals(responseCode)
                    || "502".equals(responseCode) || "503".equals(responseCode) || "504".equals(responseCode);

            if (currentRetry <= maxRetries && isRetryable) {
                batch.setStatus("RETRYING");
                batchRepository.save(batch);

                // Re-queue the batch to Redis for retry pickup by next poll cycle
                // Workers will be gated by circuit breaker cooldown — no artificial Thread.sleep()
                try {
                    Map<String, String> retryPayload = new HashMap<>();
                    retryPayload.put("campaignId", String.valueOf(campaignId));
                    retryPayload.put("batchId", String.valueOf(batchId));
                    retryPayload.put("provider", providerName != null ? providerName : "");
                    redisTemplate.opsForStream().add(STREAM_KEY, retryPayload);
                } catch (Exception requeueErr) {
                    log.error("CampaignWorkerService [{}]: Failed to re-queue batchId={} for retry: {}", nodeId, batchId, requeueErr.getMessage());
                }

                log.warn("CampaignWorkerService [{}]: Transient {} failure for batchId={}. Retry {}/{}. Error: {}",
                        nodeId, responseCode, batchId, currentRetry, maxRetries, result.getErrorMessage());
            } else {
                List<CampaignRecipientLog> failedLogs = new ArrayList<>();
                for (CampaignRecipientLog r : recipients) {
                    if ("INVALID".equals(r.getStatus())) continue;
                    r.setDurationMs((int) duration);
                    r.setStatus("FAILED");
                    r.setLastError(result.getErrorMessage());
                    r.setSmtpResponseCode(responseCode != null ? responseCode : "500");
                    failedLogs.add(r);
                    progressCache.incrementFailed(campaignId);
                }
                recipientRepository.saveAll(failedLogs);

                batch.setStatus("FAILED");
                batch.setCompletedAt(LocalDateTime.now());
                batchRepository.save(batch);

                log.error("CampaignWorkerService [{}]: Permanent batch failure for batchId={}. Error: {}", nodeId, batchId, result.getErrorMessage());
            }
        }
    }
}
