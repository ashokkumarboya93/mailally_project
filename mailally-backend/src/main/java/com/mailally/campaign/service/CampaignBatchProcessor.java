package com.mailally.campaign.service;

import com.mailally.campaign.dto.CampaignLiveProgressDto;
import com.mailally.campaign.entity.Campaign;
import com.mailally.campaign.entity.CampaignActivityLog;
import com.mailally.campaign.entity.CampaignRecipient;
import com.mailally.campaign.repository.CampaignActivityLogRepository;
import com.mailally.campaign.repository.CampaignRecipientRepository;
import com.mailally.campaign.repository.CampaignRepository;
import com.mailally.email.service.EmailEngineService;
import com.mailally.template.service.TemplateVariableEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CampaignBatchProcessor {

    private static final Logger log = LoggerFactory.getLogger(CampaignBatchProcessor.class);

    private final CampaignRepository campaignRepository;
    private final CampaignRecipientRepository recipientRepository;
    private final CampaignActivityLogRepository activityLogRepository;
    private final EmailEngineService emailEngineService;
    private final TemplateVariableEngine variableEngine;
    private final com.mailally.contact.repository.ContactRepository contactRepository;
    private final com.mailally.notification.service.NotificationService notificationService;
    private final com.mailally.email.provider.EmailProviderFactory providerFactory;

    // Track active campaign progress state in memory for SSE streaming
    private final Map<Long, CampaignLiveProgressDto> activeCampaignState = new ConcurrentHashMap<>();
    private final Map<Long, Boolean> cancelRequests = new ConcurrentHashMap<>();
    private final Map<Long, Boolean> pauseRequests = new ConcurrentHashMap<>();

    public CampaignBatchProcessor(CampaignRepository campaignRepository,
                                  CampaignRecipientRepository recipientRepository,
                                  CampaignActivityLogRepository activityLogRepository,
                                  EmailEngineService emailEngineService,
                                  TemplateVariableEngine variableEngine,
                                  com.mailally.contact.repository.ContactRepository contactRepository,
                                  com.mailally.notification.service.NotificationService notificationService,
                                  com.mailally.email.provider.EmailProviderFactory providerFactory) {
        this.campaignRepository = campaignRepository;
        this.recipientRepository = recipientRepository;
        this.activityLogRepository = activityLogRepository;
        this.emailEngineService = emailEngineService;
        this.variableEngine = variableEngine;
        this.contactRepository = contactRepository;
        this.notificationService = notificationService;
        this.providerFactory = providerFactory;
    }

    public CampaignLiveProgressDto getLiveProgress(Long campaignId) {
        CampaignLiveProgressDto dto = activeCampaignState.get(campaignId);
        if (dto == null) {
            dto = buildProgressFromDb(campaignId);
        }
        return dto;
    }

    public void pauseCampaign(Long campaignId) {
        pauseRequests.put(campaignId, true);
        logActivity(campaignId, "PAUSED", "Campaign execution paused by user.", "WARNING");
    }

    public void resumeCampaign(Long campaignId) {
        pauseRequests.put(campaignId, false);
        logActivity(campaignId, "RESUMED", "Campaign execution resumed.", "INFO");
    }

    public void cancelCampaign(Long campaignId) {
        cancelRequests.put(campaignId, true);
        logActivity(campaignId, "CANCELLED", "Campaign cancelled by user.", "ERROR");
    }

    @Async
    public void executeCampaignAsync(Long campaignId, Long orgId) {
        Campaign campaign = campaignRepository.findByIdEager(campaignId).orElse(null);
        if (campaign == null) return;

        cancelRequests.put(campaignId, false);
        pauseRequests.put(campaignId, false);

        campaign.setStatus("RUNNING");
        campaignRepository.save(campaign);

        logActivity(campaignId, "STARTED", "High-performance parallel email engine initialized.", "INFO");

        // 1. Identify existing campaign recipients
        List<CampaignRecipient> existingRecipients = recipientRepository.findByCampaignId(campaignId);
        
        // 2. Only auto-populate from organization contacts if NO specific contacts were explicitly selected for this campaign
        if (existingRecipients.isEmpty()) {
            List<com.mailally.contact.entity.Contact> contactsList = contactRepository.findByOrganizationIdAndIsDeletedFalse(orgId).stream()
                    .filter(c -> c.getEmail() != null && !c.getEmail().isBlank() && c.getEmail().contains("@"))
                    .filter(c -> c.getStatus() == null || (
                            !"UNSUBSCRIBED".equalsIgnoreCase(c.getStatus()) &&
                            !"BOUNCED".equalsIgnoreCase(c.getStatus()) &&
                            !"SPAM".equalsIgnoreCase(c.getStatus()) &&
                            !"INACTIVE".equalsIgnoreCase(c.getStatus())
                    ))
                    .collect(java.util.stream.Collectors.toList());

            List<CampaignRecipient> newlyAddedRecipients = new ArrayList<>();
            com.mailally.organization.entity.Organization org = campaign.getOrganization();
            for (com.mailally.contact.entity.Contact c : contactsList) {
                CampaignRecipient r = new CampaignRecipient();
                r.setCampaign(campaign);
                r.setContact(c);
                r.setOrganization(org);
                r.setStatus("QUEUED");
                newlyAddedRecipients.add(r);
            }
            if (!newlyAddedRecipients.isEmpty()) {
                recipientRepository.saveAll(newlyAddedRecipients);
                log.info("Auto-populated {} queued recipients for campaign ID {}", newlyAddedRecipients.size(), campaignId);
            }
        }

        // 3. Reset ALL assigned recipients for this campaign to QUEUED status for execution
        List<CampaignRecipient> allRecipients = recipientRepository.findByCampaignId(campaignId);
        for (CampaignRecipient r : allRecipients) {
            r.setStatus("QUEUED");
            r.setFailureReason(null);
            r.setSentAt(null);
            r.setDeliveredAt(null);
            r.setFailedAt(null);
        }
        recipientRepository.saveAll(allRecipients);

        long total = allRecipients.size();
        campaign.setTotalRecipients((int) total);
        campaignRepository.save(campaign);

        CampaignLiveProgressDto state = new CampaignLiveProgressDto();
        state.setCampaignId(campaignId);
        state.setStatus("RUNNING");
        state.setTotalRecipients(total);
        state.getWorkers().add(new CampaignLiveProgressDto.WorkerThreadStatusDto("Virtual-Thread-Pool-1", "Active", 0));
        state.getWorkers().add(new CampaignLiveProgressDto.WorkerThreadStatusDto("Virtual-Thread-Pool-2", "Active", 0));
        activeCampaignState.put(campaignId, state);

        int batchSize = 500; // Large high-throughput batch page size
        AtomicLong processed = new AtomicLong(0);
        AtomicLong delivered = new AtomicLong(0);
        AtomicLong failed = new AtomicLong(0);
        AtomicLong authFailures = new AtomicLong(0);
        AtomicLong connectionFailures = new AtomicLong(0);
        AtomicLong invalidRecipientFailures = new AtomicLong(0);
        AtomicLong templateFailures = new AtomicLong(0);
        AtomicLong providerErrors = new AtomicLong(0);

        long startTimeMs = System.currentTimeMillis();

        while (true) {
            if (Boolean.TRUE.equals(cancelRequests.get(campaignId))) {
                campaign.setStatus("CANCELLED");
                campaignRepository.save(campaign);
                state.setStatus("CANCELLED");
                logActivity(campaignId, "CANCELLED", "Campaign execution terminated.", "ERROR");
                break;
            }

            while (Boolean.TRUE.equals(pauseRequests.get(campaignId))) {
                state.setStatus("PAUSED");
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }

            List<CampaignRecipient> recipients = recipientRepository.findQueuedRecipients(campaignId, PageRequest.of(0, batchSize));
            if (recipients.isEmpty()) {
                break;
            }

            // Batch update status to SENDING in database to minimize SQL operations
            for (CampaignRecipient recipient : recipients) {
                recipient.setStatus("SENDING");
            }
            recipientRepository.saveAll(recipients);

            com.mailally.email.provider.EmailProvider activeProvider = providerFactory.getActiveProvider();
            if (activeProvider != null && activeProvider.supportsBulk()) {
                // High-speed Bulk Batch Dispatch via Provider Batch API (e.g. Brevo messageVersions)
                List<com.mailally.email.provider.RecipientBatchItem> batchItems = new ArrayList<>();
                List<CampaignRecipient> validRecipients = new ArrayList<>();

                final String campaignSubject = campaign.getSubject();
                final String templateHtml = campaign.getTemplate() != null ? campaign.getTemplate().getHtmlContent() : "";
                final String campaignName = campaign.getName();
                final String orgName = (campaign.getOrganization() != null) ? campaign.getOrganization().getName() : "MailAlly Organization";
                final String fromEmail = (campaign.getSenderEmail() != null && !campaign.getSenderEmail().isBlank()) 
                        ? campaign.getSenderEmail().trim() 
                        : (campaign.getFromEmail() != null ? campaign.getFromEmail().trim() : "info@marcamor.com");
                final String fromName = (campaign.getSenderName() != null && !campaign.getSenderName().isBlank()) 
                        ? campaign.getSenderName().trim() 
                        : (campaign.getFromName() != null ? campaign.getFromName().trim() : "MailAlly");

                for (CampaignRecipient recipient : recipients) {
                    if (Boolean.TRUE.equals(cancelRequests.get(campaignId))) break;

                    final com.mailally.contact.entity.Contact contact = recipient.getContact();
                    final String recipientEmail = (contact != null && contact.getEmail() != null) ? contact.getEmail().trim() : "";
                    final String recipientFirstName = (contact != null && contact.getFirstName() != null) ? contact.getFirstName() : "";
                    final String recipientLastName = (contact != null && contact.getLastName() != null) ? contact.getLastName() : "";
                    final String recipientName = (recipientFirstName + " " + recipientLastName).trim();

                    if (recipientEmail.isBlank() || !recipientEmail.contains("@")) {
                        recipient.setStatus("FAILED");
                        recipient.setFailureReason("Invalid Recipient Email Address: " + recipientEmail);
                        recipient.setFailedAt(LocalDateTime.now());
                        invalidRecipientFailures.incrementAndGet();
                        failed.incrementAndGet();
                        processed.incrementAndGet();
                        continue;
                    }

                    String personalizedSubject;
                    String personalizedBody;
                    try {
                        personalizedSubject = variableEngine.renderTemplate(campaignSubject, contact, campaignName, orgName);
                        personalizedBody = (templateHtml != null && !templateHtml.isBlank()) ? variableEngine.renderTemplate(templateHtml, contact, campaignName, orgName) : "Hello " + recipientName;
                    } catch (Exception tEx) {
                        recipient.setStatus("FAILED");
                        recipient.setFailureReason("Template Rendering Failure: " + tEx.getMessage());
                        recipient.setFailedAt(LocalDateTime.now());
                        templateFailures.incrementAndGet();
                        failed.incrementAndGet();
                        processed.incrementAndGet();
                        continue;
                    }

                    batchItems.add(new com.mailally.email.provider.RecipientBatchItem(
                            recipient.getId(),
                            recipientEmail,
                            recipientFirstName,
                            recipientLastName,
                            null,
                            personalizedSubject,
                            personalizedBody
                    ));
                    validRecipients.add(recipient);
                }

                if (!batchItems.isEmpty()) {
                    String idempotencyKey = "CMP-" + campaignId + "-B" + System.currentTimeMillis();
                    com.mailally.email.provider.BatchSendResult batchResult = providerFactory.sendBatchWithFailover(
                            batchItems,
                            fromEmail,
                            fromName,
                            fromEmail,
                            campaignSubject,
                            templateHtml,
                            idempotencyKey
                    );

                    boolean isBatchSuccess = batchResult.isSuccess();
                    Map<Long, String> msgMap = batchResult.getRecipientMessageIds();

                    for (CampaignRecipient recipient : validRecipients) {
                        if (isBatchSuccess) {
                            recipient.setStatus("DELIVERED");
                            String msgId = (msgMap != null && msgMap.containsKey(recipient.getId())) ? msgMap.get(recipient.getId()) : batchResult.getBatchMessageId();
                            recipient.setResponseId(msgId != null ? msgId : batchResult.getBatchMessageId());
                            recipient.setSentAt(LocalDateTime.now());
                            recipient.setDeliveredAt(LocalDateTime.now());
                            delivered.incrementAndGet();
                        } else {
                            recipient.setStatus("FAILED");
                            String reason = batchResult.getErrorMessage() != null ? batchResult.getErrorMessage() : "Batch dispatch failed";
                            recipient.setFailureReason(reason);
                            recipient.setFailedAt(LocalDateTime.now());
                            failed.incrementAndGet();
                            if (reason.contains("[AUTH_FAILURE]")) authFailures.incrementAndGet();
                            else if (reason.contains("[CONNECTION_FAILURE]")) connectionFailures.incrementAndGet();
                            else providerErrors.incrementAndGet();
                        }
                        processed.incrementAndGet();
                    }
                }
            } else {
                // Fallback parallel single dispatches across Virtual Threads
                try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                    for (CampaignRecipient recipient : recipients) {
                        if (Boolean.TRUE.equals(cancelRequests.get(campaignId))) break;

                        final com.mailally.contact.entity.Contact contact = recipient.getContact();
                        final String recipientEmail = (contact != null && contact.getEmail() != null) ? contact.getEmail().trim() : "";
                        final String recipientFirstName = (contact != null && contact.getFirstName() != null) ? contact.getFirstName() : "";
                        final String recipientLastName = (contact != null && contact.getLastName() != null) ? contact.getLastName() : "";
                        final String recipientName = (recipientFirstName + " " + recipientLastName).trim();
                        final String campaignSubject = campaign.getSubject();
                        final String templateHtml = campaign.getTemplate() != null ? campaign.getTemplate().getHtmlContent() : "";
                        final String campaignName = campaign.getName();
                        final String orgName = (campaign.getOrganization() != null) ? campaign.getOrganization().getName() : "MailAlly Organization";
                        final String fromEmail = (campaign.getSenderEmail() != null && !campaign.getSenderEmail().isBlank()) 
                                ? campaign.getSenderEmail().trim() 
                                : (campaign.getFromEmail() != null ? campaign.getFromEmail().trim() : "info@marcamor.com");
                        final String fromName = (campaign.getSenderName() != null && !campaign.getSenderName().isBlank()) 
                                ? campaign.getSenderName().trim() 
                                : (campaign.getFromName() != null ? campaign.getFromName().trim() : "MailAlly");

                        executor.submit(() -> {
                            try {
                                if (recipientEmail.isBlank() || !recipientEmail.contains("@")) {
                                    invalidRecipientFailures.incrementAndGet();
                                    throw new RuntimeException("Invalid Recipient Email Address: " + recipientEmail);
                                }

                                String personalizedSubject;
                                String personalizedBody;
                                try {
                                    personalizedSubject = variableEngine.renderTemplate(
                                            campaignSubject, contact, campaignName, orgName);
                                    personalizedBody = (templateHtml != null && !templateHtml.isBlank()) ? variableEngine.renderTemplate(
                                            templateHtml, contact, campaignName, orgName) : "Hello " + recipientName;
                                } catch (Exception tEx) {
                                    templateFailures.incrementAndGet();
                                    throw new RuntimeException("Template Rendering Failure: " + tEx.getMessage(), tEx);
                                }

                                com.mailally.email.provider.EmailSendResult sendResult = emailEngineService.sendEmailWithResult(
                                        recipientEmail,
                                        recipientName,
                                        fromEmail,
                                        fromName,
                                        fromEmail,
                                        personalizedSubject,
                                        personalizedBody
                                );

                                if (sendResult.isSuccess()) {
                                    recipient.setStatus("DELIVERED");
                                    if (sendResult.getResponseId() != null && !sendResult.getResponseId().isBlank()) {
                                        recipient.setResponseId(sendResult.getResponseId());
                                    }
                                    recipient.setSentAt(LocalDateTime.now());
                                    recipient.setDeliveredAt(LocalDateTime.now());
                                    delivered.incrementAndGet();
                                } else {
                                    recipient.setStatus("FAILED");
                                    String reason = sendResult.getErrorMessage() != null ? sendResult.getErrorMessage() : "Email provider dispatch returned failure response";
                                    recipient.setFailureReason(reason);
                                    recipient.setFailedAt(LocalDateTime.now());
                                    failed.incrementAndGet();

                                    if (reason.contains("[AUTH_FAILURE]")) authFailures.incrementAndGet();
                                    else if (reason.contains("[CONNECTION_FAILURE]")) connectionFailures.incrementAndGet();
                                    else if (reason.contains("[INVALID_SENDER]") || reason.contains("Invalid Recipient")) invalidRecipientFailures.incrementAndGet();
                                    else providerErrors.incrementAndGet();
                                }
                            } catch (Exception e) {
                                recipient.setStatus("FAILED");
                                String errMsg = e.getClass().getName() + ": " + e.getMessage();
                                recipient.setFailureReason(errMsg);
                                recipient.setFailedAt(LocalDateTime.now());
                                failed.incrementAndGet();
                                if (errMsg.contains("Template Rendering Failure")) {
                                    // Already counted
                                } else {
                                    providerErrors.incrementAndGet();
                                }
                            } finally {
                                processed.incrementAndGet();
                            }
                        });
                    }
                }
            }

            // Single batch save after parallel dispatches finish
            recipientRepository.saveAll(recipients);

            // Update real-time SSE progress
            long pCount = processed.get();
            long dCount = delivered.get();
            long fCount = failed.get();
            state.setSentCount(pCount);
            state.setDeliveredCount(dCount);
            state.setFailedCount(fCount);
            state.setAuthFailures(authFailures.get());
            state.setConnectionFailures(connectionFailures.get());
            state.setInvalidRecipientFailures(invalidRecipientFailures.get());
            state.setTemplateFailures(templateFailures.get());
            state.setProviderErrors(providerErrors.get());
            state.setQueuedCount(Math.max(0, total - pCount));
            int pct = total > 0 ? (int) ((pCount * 100) / total) : 100;
            state.setProgressPercentage(pct);

            long totalMs = Math.max(1, System.currentTimeMillis() - startTimeMs);
            double totalSec = totalMs / 1000.0;
            logActivity(campaignId, "BATCH_COMPLETED", 
                    String.format("Dispatched batch of %d recipients in %.2f sec.", 
                            recipients.size(), totalSec), "SUCCESS");
        }

        if (!"CANCELLED".equals(state.getStatus())) {
            campaign.setStatus("COMPLETED");
            campaign.setSentCount((int) delivered.get());
            campaign.setFailedCount((int) failed.get());
            campaignRepository.save(campaign);

            state.setStatus("COMPLETED");
            state.setProgressPercentage(100);
            long totalMs = Math.max(1, System.currentTimeMillis() - startTimeMs);
            double totalSec = totalMs / 1000.0;
            logActivity(campaignId, "FINISHED", 
                    String.format("High-speed campaign completed in %.2f seconds (%d ms)! Delivered: %d, Failed: %d.", 
                            totalSec, totalMs, delivered.get(), failed.get()), "SUCCESS");

            try {
                notificationService.sendNotification(
                        campaign.getOrganization().getId(),
                        campaign.getCreatedBy(),
                        "CAMPAIGNS",
                        "Campaign Completed: " + campaign.getName(),
                        String.format("Campaign '%s' completed dispatch. %d delivered, %d failed.", campaign.getName(), delivered.get(), failed.get()),
                        failed.get() > 0 ? "WARNING" : "SUCCESS",
                        "CAMPAIGNS",
                        campaign.getId(),
                        "/campaigns/" + campaign.getId() + "/analytics"
                );
            } catch (Exception e) {
                // Ignore notification error
            }
        }
    }

    public List<com.mailally.campaign.dto.CampaignFailureDetailDto> getFailedRecipients(Long campaignId) {
        List<CampaignRecipient> failedRecipients = recipientRepository.findByCampaignId(campaignId).stream()
                .filter(r -> "FAILED".equalsIgnoreCase(r.getStatus()))
                .collect(java.util.stream.Collectors.toList());

        List<com.mailally.campaign.dto.CampaignFailureDetailDto> list = new ArrayList<>();
        for (CampaignRecipient r : failedRecipients) {
            String email = r.getContact() != null ? r.getContact().getEmail() : "Unknown";
            String name = r.getContact() != null ? (r.getContact().getFirstName() + " " + r.getContact().getLastName()) : "Unknown";
            list.add(new com.mailally.campaign.dto.CampaignFailureDetailDto(
                    r.getId(), email, name, r.getStatus(), r.getFailureReason(), r.getRetryCount(), r.getFailedAt()
            ));
        }
        return list;
    }

    private CampaignLiveProgressDto buildProgressFromDb(Long campaignId) {
        Campaign campaign = campaignRepository.findByIdEager(campaignId).orElse(null);
        CampaignLiveProgressDto dto = new CampaignLiveProgressDto();
        dto.setCampaignId(campaignId);
        if (campaign != null) {
            dto.setStatus(campaign.getStatus());
            long total = recipientRepository.countByCampaignId(campaignId);
            long delivered = recipientRepository.countByCampaignIdAndStatus(campaignId, "DELIVERED");
            long failed = recipientRepository.countByCampaignIdAndStatus(campaignId, "FAILED");
            long queued = recipientRepository.countByCampaignIdAndStatus(campaignId, "QUEUED");
            dto.setTotalRecipients(total);
            dto.setDeliveredCount(delivered);
            dto.setFailedCount(failed);
            dto.setQueuedCount(queued);
            dto.setSentCount(delivered + failed);
            dto.setProgressPercentage(total > 0 ? (int) (((delivered + failed) * 100) / total) : 0);

            // Compute diagnostic categories from DB failure reasons
            List<CampaignRecipient> failedList = recipientRepository.findByCampaignId(campaignId).stream()
                    .filter(r -> "FAILED".equalsIgnoreCase(r.getStatus()))
                    .collect(java.util.stream.Collectors.toList());

            long authF = 0, connF = 0, invF = 0, tmplF = 0, provErr = 0;
            for (CampaignRecipient r : failedList) {
                String reason = r.getFailureReason() != null ? r.getFailureReason() : "";
                if (reason.contains("[AUTH_FAILURE]")) authF++;
                else if (reason.contains("[CONNECTION_FAILURE]")) connF++;
                else if (reason.contains("[INVALID_SENDER]") || reason.contains("Invalid Recipient")) invF++;
                else if (reason.contains("Template Rendering Failure")) tmplF++;
                else provErr++;
            }
            dto.setAuthFailures(authF);
            dto.setConnectionFailures(connF);
            dto.setInvalidRecipientFailures(invF);
            dto.setTemplateFailures(tmplF);
            dto.setProviderErrors(provErr);
        }
        return dto;
    }

    private void logActivity(Long campaignId, String eventType, String message, String severity) {
        Campaign campaign = campaignRepository.findByIdEager(campaignId).orElse(null);
        if (campaign != null) {
            CampaignActivityLog log = new CampaignActivityLog();
            log.setCampaign(campaign);
            log.setOrganization(campaign.getOrganization());
            log.setEventType(eventType);
            log.setMessage(message);
            log.setSeverity(severity);
            activityLogRepository.save(log);

            CampaignLiveProgressDto state = activeCampaignState.get(campaignId);
            if (state != null) {
                state.getRecentActivity().add(0, eventType + ": " + message);
                if (state.getRecentActivity().size() > 20) {
                    state.getRecentActivity().remove(state.getRecentActivity().size() - 1);
                }
            }
        }
    }
}
