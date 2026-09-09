package com.mailally.email.service.impl;

import com.mailally.campaign.entity.Campaign;
import com.mailally.campaign.repository.CampaignRepository;
import com.mailally.contact.entity.Contact;
import com.mailally.contact.repository.ContactRepository;
import com.mailally.email.config.EmailEngineConfig;
import com.mailally.email.dto.CampaignProgressDto;
import com.mailally.email.entity.Email;
import com.mailally.email.entity.EmailQueue;
import com.mailally.email.provider.EmailProviderFactory;
import com.mailally.email.provider.EmailSendResult;
import com.mailally.email.renderer.TemplateRenderer;
import com.mailally.email.repository.EmailQueueRepository;
import com.mailally.email.repository.EmailRepository;
import com.mailally.exception.CustomException;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Dedicated service bean for asynchronous campaign email execution.
 * <p>
 * CRITICAL: This class exists as a SEPARATE bean from {@link EmailServiceImpl} to ensure
 * Spring's proxy-based AOP correctly intercepts the {@code @Async} annotation.
 * Calling {@code @Async} methods via self-invocation within the same class is silently ignored
 * by Spring's proxy mechanism — this bean solves that fundamental issue.
 * <p>
 * This service intentionally does NOT use class-level {@code @Transactional} so that each
 * {@code repository.save()} call auto-commits immediately, enabling real-time progress tracking
 * visible to concurrent polling requests.
 */
@Service
public class CampaignAsyncExecutor {

    private static final Logger log = LoggerFactory.getLogger(CampaignAsyncExecutor.class);

    /** SSE emitter timeout: 30 minutes (for long-running campaigns) */
    private static final long SSE_TIMEOUT = 30 * 60 * 1000L;

    private final CampaignRepository campaignRepository;
    private final ContactRepository contactRepository;
    private final OrganizationRepository organizationRepository;
    private final EmailRepository emailRepository;
    private final EmailQueueRepository emailQueueRepository;
    private final com.mailally.email.repository.CampaignRecipientLogRepository recipientLogRepository;
    private final com.mailally.email.repository.EmailEventRepository emailEventRepository;
    private final EmailProviderFactory providerFactory;
    private final TemplateRenderer templateRenderer;
    private final EmailEngineConfig config;
    private final com.mailally.email.service.EmailIdempotencyService idempotencyService;
    private final com.mailally.email.service.CampaignStateMachine stateMachine;

    /** Registry of SSE emitters keyed by campaignId for live progress streaming */
    private final ConcurrentHashMap<Long, CopyOnWriteArrayList<SseEmitter>> sseEmitters = new ConcurrentHashMap<>();

    public CampaignAsyncExecutor(CampaignRepository campaignRepository,
                                 ContactRepository contactRepository,
                                 OrganizationRepository organizationRepository,
                                 EmailRepository emailRepository,
                                 EmailQueueRepository emailQueueRepository,
                                 com.mailally.email.repository.CampaignRecipientLogRepository recipientLogRepository,
                                 com.mailally.email.repository.EmailEventRepository emailEventRepository,
                                 EmailProviderFactory providerFactory,
                                 TemplateRenderer templateRenderer,
                                 EmailEngineConfig config,
                                 com.mailally.email.service.EmailIdempotencyService idempotencyService,
                                 com.mailally.email.service.CampaignStateMachine stateMachine) {
        this.campaignRepository = campaignRepository;
        this.contactRepository = contactRepository;
        this.organizationRepository = organizationRepository;
        this.emailRepository = emailRepository;
        this.emailQueueRepository = emailQueueRepository;
        this.recipientLogRepository = recipientLogRepository;
        this.emailEventRepository = emailEventRepository;
        this.providerFactory = providerFactory;
        this.templateRenderer = templateRenderer;
        this.config = config;
        this.idempotencyService = idempotencyService;
        this.stateMachine = stateMachine;
    }

    /**
     * Executes campaign email dispatch asynchronously in a background thread.
     * <p>
     * Template HTML content and subject are passed as parameters (not loaded via lazy
     * associations) to avoid {@code LazyInitializationException} since this method
     * runs outside the original HTTP request's Hibernate session.
     * <p>
     * No class-level or method-level {@code @Transactional} is used, so each
     * {@code repository.save()} auto-commits immediately — enabling real-time
     * progress visibility for concurrent polling endpoints.
     *
     * @param campaignId          the campaign ID
     * @param organizationId      the organization ID
     * @param userId              the user who initiated the campaign
     * @param batchSize           batch size for grouping
     * @param templateHtmlContent pre-loaded template HTML (avoids lazy-loading)
     * @param templateSubject     pre-loaded template subject (avoids lazy-loading)
     */
    @Async("emailTaskExecutor")
    public void executeCampaignInBackground(Long campaignId, Long organizationId, Long userId,
                                            Integer batchSize, String templateHtmlContent, String templateSubject) {
        try {
            // Load entities fresh in this thread — simple findById (no lazy proxy issues)
            Campaign campaign = campaignRepository.findById(campaignId)
                    .orElseThrow(() -> new CustomException("Campaign not found: " + campaignId));
            Organization org = organizationRepository.findById(organizationId)
                    .orElseThrow(() -> new CustomException("Organization not found: " + organizationId));

            List<Contact> contacts = contactRepository.findByOrganizationIdAndIsDeletedFalse(organizationId).stream()
                    .filter(c -> c.getEmail() != null && !c.getEmail().isBlank() && c.getEmail().contains("@"))
                    .filter(c -> c.getStatus() == null || (
                            !"UNSUBSCRIBED".equalsIgnoreCase(c.getStatus()) &&
                            !"BOUNCED".equalsIgnoreCase(c.getStatus()) &&
                            !"SPAM".equalsIgnoreCase(c.getStatus()) &&
                            !"INACTIVE".equalsIgnoreCase(c.getStatus())
                    ))
                    .collect(Collectors.toList());

            String fromName = campaign.getFromName() != null ? campaign.getFromName()
                    : campaign.getSenderName() != null ? campaign.getSenderName() : config.getDefaultSenderName();
            String fromEmail = campaign.getSenderEmail() != null ? campaign.getSenderEmail() : config.getDefaultSenderEmail();
            int effectiveBatchSize = batchSize != null && batchSize > 0 ? batchSize : 500;
            int totalContacts = contacts.size();

            java.util.concurrent.ConcurrentLinkedQueue<Email> emailLogsToSave = new java.util.concurrent.ConcurrentLinkedQueue<>();
            java.util.concurrent.ConcurrentLinkedQueue<EmailQueue> queueToSave = new java.util.concurrent.ConcurrentLinkedQueue<>();
            java.util.concurrent.ConcurrentLinkedQueue<com.mailally.email.entity.CampaignRecipientLog> recipientLogsToSave = new java.util.concurrent.ConcurrentLinkedQueue<>();
            java.util.concurrent.ConcurrentLinkedQueue<com.mailally.email.entity.EmailEvent> eventsToSave = new java.util.concurrent.ConcurrentLinkedQueue<>();

            java.util.concurrent.atomic.AtomicInteger sentCounter = new java.util.concurrent.atomic.AtomicInteger(0);
            java.util.concurrent.atomic.AtomicInteger failedCounter = new java.util.concurrent.atomic.AtomicInteger(0);
            java.util.concurrent.atomic.AtomicInteger indexCounter = new java.util.concurrent.atomic.AtomicInteger(0);
            java.util.concurrent.atomic.AtomicBoolean cancelled = new java.util.concurrent.atomic.AtomicBoolean(false);

            // Enforce Campaign State Machine validation
            stateMachine.validateTransition(campaign.getStatus(), "RUNNING");
            campaign.setStatus("RUNNING");
            campaignRepository.save(campaign);

            log.info("=== ASYNC CAMPAIGN [{}] HIGH-SPEED BATCH DISPATCH STARTED — {} recipients, Provider: {} ===",
                    campaign.getName(), totalContacts, config.getActiveProvider());

            int chunkSize = Math.min(effectiveBatchSize, 300);
            List<List<Contact>> contactBatches = new ArrayList<>();
            for (int i = 0; i < contacts.size(); i += chunkSize) {
                contactBatches.add(contacts.subList(i, Math.min(i + chunkSize, contacts.size())));
            }

            int batchIndex = 0;
            for (List<Contact> contactChunk : contactBatches) {
                if (cancelled.get()) {
                    break;
                }

                batchIndex++;
                Campaign freshCampaign = campaignRepository.findById(campaignId).orElse(null);
                if (freshCampaign != null && "CANCELLED".equalsIgnoreCase(freshCampaign.getStatus())) {
                    cancelled.set(true);
                    log.warn("Campaign {} cancelled mid-execution at batch {}/{}", campaignId, batchIndex, contactBatches.size());
                    break;
                }

                List<com.mailally.email.provider.RecipientBatchItem> batchItems = new ArrayList<>();
                List<Contact> validContactsInBatch = new ArrayList<>();

                for (Contact contact : contactChunk) {
                    if (idempotencyService.isAlreadyProcessed(campaignId, contact.getEmail())) {
                        log.info("Idempotency Guard: Skipping processed recipient {} for campaign {}", contact.getEmail(), campaignId);
                        sentCounter.incrementAndGet();
                        continue;
                    }
                    idempotencyService.markProcessed(campaignId, contact.getEmail());

                    String personalizedSubject = templateRenderer.render(
                            campaign.getSubject() != null ? campaign.getSubject() : templateSubject, contact);
                    String personalizedBody = templateRenderer.render(templateHtmlContent, contact);

                    batchItems.add(new com.mailally.email.provider.RecipientBatchItem(
                            null,
                            contact.getEmail(),
                            contact.getFirstName(),
                            contact.getLastName(),
                            null,
                            personalizedSubject,
                            personalizedBody
                    ));
                    validContactsInBatch.add(contact);
                }

                if (batchItems.isEmpty()) {
                    continue;
                }

                String idempotencyKey = "CMP-" + campaignId + "-B" + batchIndex + "-" + System.currentTimeMillis();

                // High-speed bulk batch dispatch (Brevo/SES REST API)
                com.mailally.email.provider.BatchSendResult batchResult = providerFactory.sendBatchWithFailover(
                        batchItems,
                        fromEmail,
                        fromName,
                        campaign.getReplyTo(),
                        campaign.getSubject() != null ? campaign.getSubject() : templateSubject,
                        templateHtmlContent,
                        idempotencyKey
                );

                boolean isBatchSuccess = batchResult.isSuccess();
                String providerUsed = batchResult.getProviderName();

                for (int j = 0; j < validContactsInBatch.size(); j++) {
                    Contact contact = validContactsInBatch.get(j);
                    com.mailally.email.provider.RecipientBatchItem item = batchItems.get(j);
                    String messageId = batchResult.getRecipientMsgIdMap() != null ? batchResult.getRecipientMsgIdMap().get(item.getRecipientLogId()) : batchResult.getBatchMessageId();
                    if (messageId == null) messageId = batchResult.getBatchMessageId();

                    // Buffer email log
                    emailLogsToSave.add(Email.builder()
                            .organization(org)
                            .campaign(campaign)
                            .contact(contact)
                            .recipientEmail(contact.getEmail())
                            .recipientName(contact.getFirstName())
                            .subject(item.getPersonalizedSubject())
                            .provider(providerUsed)
                            .status(isBatchSuccess ? "SENT" : "FAILED")
                            .responseId(messageId)
                            .errorMessage(isBatchSuccess ? null : batchResult.getErrorMessage())
                            .sentAt(isBatchSuccess ? LocalDateTime.now() : null)
                            .failedAt(isBatchSuccess ? null : LocalDateTime.now())
                            .createdBy(userId)
                            .build());

                    // Buffer queue entry
                    queueToSave.add(EmailQueue.builder()
                            .organization(org)
                            .campaign(campaign)
                            .contact(contact)
                            .recipientEmail(contact.getEmail())
                            .recipientName(contact.getFirstName())
                            .personalizedSubject(item.getPersonalizedSubject())
                            .personalizedHtml(item.getPersonalizedHtml())
                            .provider(providerUsed)
                            .status(isBatchSuccess ? "SENT" : "FAILED")
                            .retryCount(0)
                            .maxRetries(config.getMaxRetries())
                            .failureReason(isBatchSuccess ? null : batchResult.getErrorMessage())
                            .batchNumber(batchIndex)
                            .processedAt(LocalDateTime.now())
                            .createdBy(userId)
                            .build());

                    // Buffer recipient log
                    com.mailally.email.entity.CampaignRecipientLog recipientLog = com.mailally.email.entity.CampaignRecipientLog.builder()
                            .campaign(campaign)
                            .contact(contact)
                            .email(contact.getEmail())
                            .status(isBatchSuccess ? "SENT" : "FAILED")
                            .provider(providerUsed)
                            .providerMessageId(messageId)
                            .attempts(1)
                            .lastError(isBatchSuccess ? null : batchResult.getErrorMessage())
                            .smtpResponseCode(isBatchSuccess ? "250 OK" : batchResult.getErrorCode())
                            .createdAt(LocalDateTime.now())
                            .build();
                    recipientLogsToSave.add(recipientLog);

                    // Buffer immutable EmailEvent
                    eventsToSave.add(com.mailally.email.entity.EmailEvent.builder()
                            .organizationId(organizationId)
                            .campaign(campaign)
                            .recipient(recipientLog)
                            .eventType(isBatchSuccess ? com.mailally.email.constant.EmailEventType.SENT : com.mailally.email.constant.EmailEventType.BOUNCED)
                            .provider(providerUsed)
                            .providerMessageId(messageId)
                            .timestamp(LocalDateTime.now())
                            .occurredAt(LocalDateTime.now())
                            .build());
                }

                if (isBatchSuccess) {
                    sentCounter.addAndGet(validContactsInBatch.size());
                } else {
                    failedCounter.addAndGet(validContactsInBatch.size());
                }

                int currentSent = sentCounter.get();
                int currentFailed = failedCounter.get();
                int pending = totalContacts - currentSent - currentFailed;

                // Broadcast live SSE progress updates per batch
                CampaignProgressDto progressDto = buildProgressDto(campaign, totalContacts, currentSent, currentFailed, pending, "RUNNING");
                emitProgressEvent(campaignId, progressDto);
            }

            if (cancelled.get()) {
                emitProgressEvent(campaignId, buildProgressDto(campaign, totalContacts, sentCounter.get(), failedCounter.get(),
                        totalContacts - sentCounter.get() - failedCounter.get(), "CANCELLED"));
                return;
            }

            // High-Speed Bulk Batch Database Persistence
            flushBatchLogs(emailLogsToSave, queueToSave, recipientLogsToSave, eventsToSave);

            int finalSent = sentCounter.get();
            int finalFailed = failedCounter.get();

            // Mark campaign completed
            campaign.setStatus("COMPLETED");
            campaign.setSentCount(finalSent);
            campaign.setFailedCount(finalFailed);
            campaignRepository.save(campaign);

            // Emit final COMPLETED event
            CampaignProgressDto completedDto = buildProgressDto(campaign, totalContacts, finalSent, finalFailed, 0, "COMPLETED");
            emitProgressEvent(campaignId, completedDto);

            // Complete and cleanup all emitters for this campaign
            completeAllEmitters(campaignId);

            log.info("=== ASYNC CAMPAIGN [{}] COMPLETED — Sent: {}, Failed: {}, Total: {} ===",
                    campaign.getName(), finalSent, finalFailed, totalContacts);

        } catch (Exception ex) {
            log.error("Async campaign execution FAILED for campaignId {}: {}", campaignId, ex.getMessage(), ex);
            try {
                Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
                if (campaign != null) {
                    campaign.setStatus("FAILED");
                    campaignRepository.save(campaign);
                }
            } catch (Exception innerEx) {
                log.error("Failed to update campaign status after error: {}", innerEx.getMessage());
            }
            completeAllEmitters(campaignId);
        }
    }

    /**
     * Executes high-performance bulk batch database persistence.
     */
    private void flushBatchLogs(
            java.util.concurrent.ConcurrentLinkedQueue<Email> emailLogsToSave,
            java.util.concurrent.ConcurrentLinkedQueue<EmailQueue> queueToSave,
            java.util.concurrent.ConcurrentLinkedQueue<com.mailally.email.entity.CampaignRecipientLog> recipientLogsToSave,
            java.util.concurrent.ConcurrentLinkedQueue<com.mailally.email.entity.EmailEvent> eventsToSave) {

        if (!emailLogsToSave.isEmpty()) {
            List<Email> emails = new ArrayList<>();
            Email item;
            while ((item = emailLogsToSave.poll()) != null) {
                emails.add(item);
            }
            if (!emails.isEmpty()) {
                emailRepository.saveAll(emails);
            }
        }

        if (!queueToSave.isEmpty()) {
            List<EmailQueue> queues = new ArrayList<>();
            EmailQueue item;
            while ((item = queueToSave.poll()) != null) {
                queues.add(item);
            }
            if (!queues.isEmpty()) {
                emailQueueRepository.saveAll(queues);
            }
        }

        if (!recipientLogsToSave.isEmpty()) {
            List<com.mailally.email.entity.CampaignRecipientLog> logs = new ArrayList<>();
            com.mailally.email.entity.CampaignRecipientLog item;
            while ((item = recipientLogsToSave.poll()) != null) {
                logs.add(item);
            }
            if (!logs.isEmpty()) {
                recipientLogRepository.saveAll(logs);
            }
        }

        if (!eventsToSave.isEmpty()) {
            List<com.mailally.email.entity.EmailEvent> events = new ArrayList<>();
            com.mailally.email.entity.EmailEvent item;
            while ((item = eventsToSave.poll()) != null) {
                events.add(item);
            }
            if (!events.isEmpty()) {
                emailEventRepository.saveAll(events);
            }
        }
    }

    // =====================================================================
    // SSE LIVE PROGRESS STREAMING
    // =====================================================================

    /**
     * Creates and registers an SSE emitter for live campaign progress streaming.
     */
    public SseEmitter createProgressEmitter(Long campaignId, Campaign campaign) {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);

        // Register this emitter
        sseEmitters.computeIfAbsent(campaignId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        // Cleanup on completion/timeout/error
        emitter.onCompletion(() -> removeEmitter(campaignId, emitter));
        emitter.onTimeout(() -> removeEmitter(campaignId, emitter));
        emitter.onError(ex -> removeEmitter(campaignId, emitter));

        log.info("SSE client connected for campaign progress: campaignId={}", campaignId);

        // Send initial current state immediately
        try {
            if (campaign != null) {
                int total = campaign.getTotalRecipients() != null ? campaign.getTotalRecipients() : 0;
                int sent = campaign.getSentCount() != null ? campaign.getSentCount() : 0;
                int failed = campaign.getFailedCount() != null ? campaign.getFailedCount() : 0;
                int pending = total - sent - failed;
                CampaignProgressDto currentProgress = buildProgressDto(campaign, total, sent, failed,
                        Math.max(pending, 0), campaign.getStatus());
                emitter.send(SseEmitter.event()
                        .name("progress")
                        .data(currentProgress));
            }
        } catch (IOException e) {
            log.warn("Failed to send initial SSE event for campaign {}: {}", campaignId, e.getMessage());
        }

        return emitter;
    }

    /**
     * Broadcasts a progress event to all SSE emitters registered for the given campaign.
     */
    private void emitProgressEvent(Long campaignId, CampaignProgressDto progressDto) {
        CopyOnWriteArrayList<SseEmitter> emitters = sseEmitters.get(campaignId);
        if (emitters == null || emitters.isEmpty()) {
            return;
        }

        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("progress")
                        .data(progressDto));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        }
        emitters.removeAll(deadEmitters);
    }

    /**
     * Completes all SSE emitters for a campaign (called when campaign finishes).
     */
    private void completeAllEmitters(Long campaignId) {
        CopyOnWriteArrayList<SseEmitter> emitters = sseEmitters.remove(campaignId);
        if (emitters != null) {
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.complete();
                } catch (Exception ignored) {
                    // Already completed or timed out
                }
            }
        }
    }

    /**
     * Removes a single emitter from the registry (called on completion/timeout/error).
     */
    private void removeEmitter(Long campaignId, SseEmitter emitter) {
        CopyOnWriteArrayList<SseEmitter> emitters = sseEmitters.get(campaignId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                sseEmitters.remove(campaignId);
            }
        }
    }

    /**
     * Helper to build a CampaignProgressDto from current state.
     */
    private CampaignProgressDto buildProgressDto(Campaign campaign, int total, int sent, int failed, int pending, String status) {
        double progress = total > 0 ? ((double) (sent + failed) / total) * 100.0 : 0.0;
        return CampaignProgressDto.builder()
                .campaignId(campaign.getId())
                .campaignName(campaign.getName())
                .campaignStatus(status)
                .totalRecipients(total)
                .sentCount(sent)
                .failedCount(failed)
                .pendingCount(Math.max(pending, 0))
                .progressPercentage(Math.min(progress, 100.0))
                .build();
    }
}
