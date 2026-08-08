package com.mailally.email.worker;

import com.mailally.campaign.entity.Campaign;
import com.mailally.campaign.repository.CampaignRepository;
import com.mailally.email.cache.RedisProgressCache;
import com.mailally.email.entity.CampaignBatch;
import com.mailally.email.entity.CampaignRecipientLog;
import com.mailally.email.provider.EmailProvider;
import com.mailally.email.provider.EmailProviderFactory;
import com.mailally.email.provider.EmailSendResult;
import com.mailally.email.renderer.TemplateRenderer;
import com.mailally.email.repository.CampaignBatchRepository;
import com.mailally.email.repository.CampaignRecipientLogRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Worker service executing batches concurrently using Java virtual threads.
 */
@Service
public class CampaignWorkerService {

    private static final Logger log = LoggerFactory.getLogger(CampaignWorkerService.class);
    private static final String STREAM_KEY = "campaign:queue:pending";
    private static final String GROUP_NAME = "campaign-workers-group";

    private final String nodeId = "worker-node-" + UUID.randomUUID().toString().substring(0, 8);

    private final StringRedisTemplate redisTemplate;
    private final CampaignBatchRepository batchRepository;
    private final CampaignRecipientLogRepository recipientRepository;
    private final CampaignRepository campaignRepository;
    private final EmailProviderFactory providerFactory;
    private final TemplateRenderer templateRenderer;
    private final RedisProgressCache progressCache;
    private final ExecutorService workerExecutor;

    public CampaignWorkerService(StringRedisTemplate redisTemplate,
                                 CampaignBatchRepository batchRepository,
                                 CampaignRecipientLogRepository recipientRepository,
                                 CampaignRepository campaignRepository,
                                 EmailProviderFactory providerFactory,
                                 TemplateRenderer templateRenderer,
                                 RedisProgressCache progressCache) {
        this.redisTemplate = redisTemplate;
        this.batchRepository = batchRepository;
        this.recipientRepository = recipientRepository;
        this.campaignRepository = campaignRepository;
        this.providerFactory = providerFactory;
        this.templateRenderer = templateRenderer;
        this.progressCache = progressCache;
        // Spawns Java virtual threads for light concurrent network dispatches
        this.workerExecutor = Executors.newVirtualThreadPerTaskExecutor();
    }

    @PostConstruct
    public void initGroup() {
        try {
            redisTemplate.opsForStream().createGroup(STREAM_KEY, ReadOffset.from("0-0"), GROUP_NAME);
            log.info("CampaignWorkerService [{}]: Consumer group '{}' successfully initialized.", nodeId, GROUP_NAME);
        } catch (Exception e) {
            log.debug("Consumer group initialization skipped: {}", e.getMessage());
        }
    }

    @Scheduled(fixedDelay = 500)
    public void pollQueue() {
        try {
            List<MapRecord<String, Object, Object>> records = redisTemplate.opsForStream().read(
                org.springframework.data.redis.connection.stream.Consumer.from(GROUP_NAME, nodeId),
                StreamReadOptions.empty().count(10),
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
        log.info("CampaignWorkerService [{}]: Starting execution for batchId={}", nodeId, batchId);

        CampaignBatch batch = batchRepository.findById(batchId).orElse(null);
        if (batch == null) {
            log.error("Batch not found in database: {}", batchId);
            return;
        }

        batch.setStatus("RUNNING");
        batch.setWorkerNodeId(nodeId);
        batch.setStartedAt(LocalDateTime.now());
        batchRepository.save(batch);

        Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
        if (campaign == null) {
            log.error("Campaign not found for batch execution: {}", campaignId);
            batch.setStatus("FAILED");
            batchRepository.save(batch);
            return;
        }

        List<CampaignRecipientLog> allQueued = recipientRepository.findByCampaignIdAndStatus(campaignId, "QUEUED");
        if (allQueued.isEmpty()) {
            log.info("No queued recipients found for campaignId={} batchId={}", campaignId, batchId);
            batch.setStatus("COMPLETED");
            batch.setCompletedAt(LocalDateTime.now());
            batchRepository.save(batch);
            return;
        }

        int chunkSize = batch.getOptimalSize() != null ? batch.getOptimalSize() : 50;
        List<CampaignRecipientLog> recipients = allQueued.subList(0, Math.min(allQueued.size(), chunkSize));

        EmailProvider provider = providerFactory.getProvider(providerName);
        String fromName = campaign.getFromName() != null ? campaign.getFromName() : "MailAlly";
        String fromEmail = campaign.getSenderEmail() != null ? campaign.getSenderEmail() : "info@marcamor.com";

        int sent = 0;
        int failed = 0;

        for (CampaignRecipientLog recipient : recipients) {
            long startTime = System.currentTimeMillis();
            recipient.setWorkerThreadId(Thread.currentThread().getName());
            recipient.setAttempts(recipient.getAttempts() + 1);

            try {
                String renderedSubject = templateRenderer.render(
                        campaign.getSubject() != null ? campaign.getSubject() : campaign.getTemplate().getSubject(),
                        recipient.getContact()
                );
                String renderedBody = templateRenderer.render(
                        campaign.getTemplate().getHtmlContent(),
                        recipient.getContact()
                );

                EmailSendResult result = provider.send(
                        recipient.getEmail(),
                        recipient.getContact() != null ? recipient.getContact().getFirstName() : "",
                        fromEmail,
                        fromName,
                        campaign.getReplyTo(),
                        renderedSubject,
                        renderedBody
                );

                long duration = System.currentTimeMillis() - startTime;
                recipient.setDurationMs((int) duration);

                if (result.isSuccess()) {
                    recipient.setStatus("SENT");
                    recipient.setProviderMessageId(result.getResponseId());
                    recipient.setSmtpResponseCode(result.getSmtpResponseCode() != null ? result.getSmtpResponseCode() : "250 OK");
                    progressCache.incrementSent(campaignId);
                    sent++;
                } else {
                    String failureReason = result.getFailureCategory() != null ? result.getFailureCategory() : "PROVIDER_REJECTED";
                    recipient.setStatus(failureReason);
                    recipient.setLastError(result.getErrorMessage());
                    recipient.setSmtpResponseCode(result.getSmtpResponseCode() != null ? result.getSmtpResponseCode() : "500");
                    progressCache.incrementFailed(campaignId);
                    failed++;

                    log.error("[FAILED EMAIL LOG] Campaign ID: {} | Recipient: {} | Provider: {} | SMTP Response: {} | Provider Message ID: {} | Duration: {}ms | Exception: {} | Worker: {} | Correlation ID: {}",
                            campaignId, recipient.getEmail(), provider.getProviderName(), recipient.getSmtpResponseCode(),
                            result.getResponseId() != null ? result.getResponseId() : "N/A", duration, result.getErrorMessage(),
                            Thread.currentThread().getName(), batchId);
                }
            } catch (Exception ex) {
                long duration = System.currentTimeMillis() - startTime;
                recipient.setDurationMs((int) duration);
                recipient.setStatus("TEMPLATE_ERROR");
                recipient.setLastError(ex.getMessage() != null ? ex.getMessage() : ex.toString());
                recipient.setSmtpResponseCode("500");
                progressCache.incrementFailed(campaignId);
                failed++;

                log.error("[FAILED EMAIL LOG] Campaign ID: {} | Recipient: {} | Provider: {} | SMTP Response: 500 | Provider Message ID: N/A | Duration: {}ms | Exception: {} | Worker: {} | Correlation ID: {}",
                        campaignId, recipient.getEmail(), providerName, duration, ex.getMessage(), Thread.currentThread().getName(), batchId, ex);
            }

            recipientRepository.save(recipient);
        }

        batch.setStatus(failed > 0 ? "COMPLETED_WITH_ERRORS" : "COMPLETED");
        batch.setCompletedAt(LocalDateTime.now());
        batchRepository.save(batch);

        log.info("CampaignWorkerService [{}]: Completed batchId={}. Sent: {}, Failed: {}", nodeId, batchId, sent, failed);
    }
}
