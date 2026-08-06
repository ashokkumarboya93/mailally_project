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
    private final EmailProviderFactory providerFactory;
    private final TemplateRenderer templateRenderer;
    private final EmailEngineConfig config;

    /** Registry of SSE emitters keyed by campaignId for live progress streaming */
    private final ConcurrentHashMap<Long, CopyOnWriteArrayList<SseEmitter>> sseEmitters = new ConcurrentHashMap<>();

    public CampaignAsyncExecutor(CampaignRepository campaignRepository,
                                 ContactRepository contactRepository,
                                 OrganizationRepository organizationRepository,
                                 EmailRepository emailRepository,
                                 EmailQueueRepository emailQueueRepository,
                                 EmailProviderFactory providerFactory,
                                 TemplateRenderer templateRenderer,
                                 EmailEngineConfig config) {
        this.campaignRepository = campaignRepository;
        this.contactRepository = contactRepository;
        this.organizationRepository = organizationRepository;
        this.emailRepository = emailRepository;
        this.emailQueueRepository = emailQueueRepository;
        this.providerFactory = providerFactory;
        this.templateRenderer = templateRenderer;
        this.config = config;
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

            int sent = 0;
            int failed = 0;
            int index = 0;
            int totalContacts = contacts.size();

            log.info("=== ASYNC CAMPAIGN [{}] STARTED — {} recipients, Provider: {} ===",
                    campaign.getName(), totalContacts, config.getActiveProvider());

            for (Contact contact : contacts) {
                // Check if campaign was cancelled mid-execution
                Campaign freshCampaign = campaignRepository.findById(campaignId).orElse(null);
                if (freshCampaign != null && "CANCELLED".equalsIgnoreCase(freshCampaign.getStatus())) {
                    log.warn("Campaign {} cancelled mid-execution at index {}/{}", campaignId, index, totalContacts);
                    emitProgressEvent(campaignId, buildProgressDto(campaign, totalContacts, sent, failed,
                            totalContacts - sent - failed, "CANCELLED"));
                    return;
                }

                int batchNumber = (index / effectiveBatchSize) + 1;

                // Render personalized content using pre-loaded template data (NOT lazy-loaded)
                String personalizedSubject = templateRenderer.render(
                        campaign.getSubject() != null ? campaign.getSubject() : templateSubject, contact);
                String personalizedBody = templateRenderer.render(templateHtmlContent, contact);

                // Send email via provider with failover
                EmailSendResult result = providerFactory.sendWithFailover(
                        contact.getEmail(),
                        contact.getFirstName(),
                        fromEmail,
                        fromName,
                        campaign.getReplyTo(),
                        personalizedSubject,
                        personalizedBody
                );

                // Save email log — uses directly-loaded org (NOT campaign.getOrganization() which is a lazy proxy)
                Email emailLog = Email.builder()
                        .organization(org)
                        .campaign(campaign)
                        .contact(contact)
                        .recipientEmail(contact.getEmail())
                        .recipientName(contact.getFirstName())
                        .subject(personalizedSubject)
                        .provider(result.getProviderName())
                        .status(result.isSuccess() ? "SENT" : "FAILED")
                        .responseId(result.getResponseId())
                        .errorMessage(result.getErrorMessage())
                        .sentAt(result.isSuccess() ? LocalDateTime.now() : null)
                        .failedAt(result.isSuccess() ? null : LocalDateTime.now())
                        .createdBy(userId)
                        .build();
                emailRepository.save(emailLog);

                // Save queue entry
                emailQueueRepository.save(EmailQueue.builder()
                        .organization(org)
                        .campaign(campaign)
                        .contact(contact)
                        .recipientEmail(contact.getEmail())
                        .recipientName(contact.getFirstName())
                        .personalizedSubject(personalizedSubject)
                        .personalizedHtml(personalizedBody)
                        .provider(result.getProviderName())
                        .status(result.isSuccess() ? "SENT" : "FAILED")
                        .retryCount(0)
                        .maxRetries(config.getMaxRetries())
                        .failureReason(result.getErrorMessage())
                        .batchNumber(batchNumber)
                        .processedAt(LocalDateTime.now())
                        .createdBy(userId)
                        .build());

                if (result.isSuccess()) {
                    sent++;
                } else {
                    failed++;
                }
                index++;

                // Update campaign progress — auto-commits immediately (no surrounding transaction)
                campaign.setSentCount(sent);
                campaign.setFailedCount(failed);
                campaignRepository.save(campaign);

                // Emit SSE progress event to all connected clients
                int pending = totalContacts - sent - failed;
                CampaignProgressDto progressDto = buildProgressDto(campaign, totalContacts, sent, failed, pending, "RUNNING");
                emitProgressEvent(campaignId, progressDto);

                if (index % 10 == 0) {
                    log.info("Campaign [{}] progress: {}/{} sent, {}/{} failed, {}/{} remaining",
                            campaign.getName(), sent, totalContacts, failed, totalContacts, pending, totalContacts);
                }
            }

            // Mark campaign completed
            campaign.setStatus("COMPLETED");
            campaign.setSentCount(sent);
            campaign.setFailedCount(failed);
            campaignRepository.save(campaign);

            // Emit final COMPLETED event
            CampaignProgressDto completedDto = buildProgressDto(campaign, totalContacts, sent, failed, 0, "COMPLETED");
            emitProgressEvent(campaignId, completedDto);

            // Complete and cleanup all emitters for this campaign
            completeAllEmitters(campaignId);

            log.info("=== ASYNC CAMPAIGN [{}] COMPLETED — Sent: {}, Failed: {}, Total: {} ===",
                    campaign.getName(), sent, failed, totalContacts);

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
