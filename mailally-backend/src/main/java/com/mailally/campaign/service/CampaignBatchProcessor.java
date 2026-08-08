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

    // Track active campaign progress state in memory for SSE streaming
    private final Map<Long, CampaignLiveProgressDto> activeCampaignState = new ConcurrentHashMap<>();
    private final Map<Long, Boolean> cancelRequests = new ConcurrentHashMap<>();
    private final Map<Long, Boolean> pauseRequests = new ConcurrentHashMap<>();

    public CampaignBatchProcessor(CampaignRepository campaignRepository,
                                  CampaignRecipientRepository recipientRepository,
                                  CampaignActivityLogRepository activityLogRepository,
                                  EmailEngineService emailEngineService,
                                  TemplateVariableEngine variableEngine,
                                  com.mailally.contact.repository.ContactRepository contactRepository) {
        this.campaignRepository = campaignRepository;
        this.recipientRepository = recipientRepository;
        this.activityLogRepository = activityLogRepository;
        this.emailEngineService = emailEngineService;
        this.variableEngine = variableEngine;
        this.contactRepository = contactRepository;
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

        long total = recipientRepository.countByCampaignId(campaignId);
        if (total == 0) {
            // Auto-populate recipients from contacts table for this organization!
            List<com.mailally.contact.entity.Contact> contactsList = contactRepository.findByOrganizationIdAndIsDeletedFalse(orgId).stream()
                    .filter(c -> c.getEmail() != null && !c.getEmail().isBlank() && c.getEmail().contains("@"))
                    .filter(c -> c.getStatus() == null || (
                            !"UNSUBSCRIBED".equalsIgnoreCase(c.getStatus()) &&
                            !"BOUNCED".equalsIgnoreCase(c.getStatus()) &&
                            !"SPAM".equalsIgnoreCase(c.getStatus()) &&
                            !"INACTIVE".equalsIgnoreCase(c.getStatus())
                    ))
                    .collect(java.util.stream.Collectors.toList());

            List<CampaignRecipient> newRecipients = new ArrayList<>();
            com.mailally.organization.entity.Organization org = campaign.getOrganization();
            for (com.mailally.contact.entity.Contact c : contactsList) {
                CampaignRecipient r = new CampaignRecipient();
                r.setCampaign(campaign);
                r.setContact(c);
                r.setOrganization(org);
                r.setStatus("QUEUED");
                newRecipients.add(r);
            }
            if (!newRecipients.isEmpty()) {
                recipientRepository.saveAll(newRecipients);
                total = newRecipients.size();
                campaign.setTotalRecipients((int) total);
                campaignRepository.save(campaign);
                log.info("Auto-populated {} queued recipients for campaign ID {}", total, campaignId);
            }
        } else {
            // Reset existing recipients to QUEUED status on relaunch so they are re-sent
            List<CampaignRecipient> existing = recipientRepository.findByCampaignId(campaignId);
            for (CampaignRecipient r : existing) {
                r.setStatus("QUEUED");
                r.setFailureReason(null);
                r.setSentAt(null);
                r.setDeliveredAt(null);
                r.setFailedAt(null);
            }
            recipientRepository.saveAll(existing);
        }

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

            // Execute parallel dispatches across Virtual Threads
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (CampaignRecipient recipient : recipients) {
                    if (Boolean.TRUE.equals(cancelRequests.get(campaignId))) break;

                    final String orgName = (campaign.getOrganization() != null) ? campaign.getOrganization().getName() : "MailAlly Organization";

                    executor.submit(() -> {
                        try {
                            String personalizedSubject;
                            String personalizedBody;
                            try {
                                personalizedSubject = variableEngine.renderTemplate(
                                        campaign.getSubject(), recipient.getContact(), campaign.getName(), orgName);
                                personalizedBody = campaign.getTemplate() != null ? variableEngine.renderTemplate(
                                        campaign.getTemplate().getHtmlContent(), recipient.getContact(), campaign.getName(), orgName) : "";
                            } catch (Exception tEx) {
                                templateFailures.incrementAndGet();
                                throw new RuntimeException("Template Rendering Failure: " + tEx.getMessage(), tEx);
                            }

                            String fromEmail = (campaign.getSenderEmail() != null && !campaign.getSenderEmail().isBlank()) 
                                    ? campaign.getSenderEmail().trim() 
                                    : (campaign.getFromEmail() != null ? campaign.getFromEmail().trim() : "info@marcamor.com");
                            String fromName = (campaign.getSenderName() != null && !campaign.getSenderName().isBlank()) 
                                    ? campaign.getSenderName().trim() 
                                    : (campaign.getFromName() != null ? campaign.getFromName().trim() : "Marcamor");

                            com.mailally.email.provider.EmailSendResult sendResult = emailEngineService.sendEmailWithResult(
                                    recipient.getContact().getEmail(),
                                    recipient.getContact().getFirstName() + " " + recipient.getContact().getLastName(),
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
            } // Auto-closes and waits for all parallel virtual threads in batch to complete

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

            long elapsedSec = Math.max(1, (System.currentTimeMillis() - startTimeMs) / 1000);
            logActivity(campaignId, "BATCH_COMPLETED", 
                    String.format("Dispatched parallel batch of %d recipients in %d sec (Speed: %d msgs/sec).", 
                            recipients.size(), elapsedSec, (pCount / elapsedSec)), "SUCCESS");
        }

        if (!"CANCELLED".equals(state.getStatus())) {
            campaign.setStatus("COMPLETED");
            campaign.setSentCount((int) delivered.get());
            campaign.setFailedCount((int) failed.get());
            campaignRepository.save(campaign);

            state.setStatus("COMPLETED");
            state.setProgressPercentage(100);
            long totalTimeSec = Math.max(1, (System.currentTimeMillis() - startTimeMs) / 1000);
            logActivity(campaignId, "FINISHED", 
                    String.format("High-speed campaign completed in %d seconds! Delivered: %d, Failed: %d (Avg throughput: %d emails/sec).", 
                            totalTimeSec, delivered.get(), failed.get(), (total > 0 ? total / totalTimeSec : 0)), "SUCCESS");
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
