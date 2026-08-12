package com.mailally.email.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailally.contact.entity.Contact;
import com.mailally.contact.repository.ContactRepository;
import com.mailally.email.entity.CampaignRecipientLog;
import com.mailally.email.entity.EmailEvent;
import com.mailally.email.entity.UnresolvedWebhookEvent;
import com.mailally.email.repository.CampaignRecipientLogRepository;
import com.mailally.email.repository.EmailRepository;
import com.mailally.email.repository.EmailEventRepository;
import com.mailally.email.repository.UnresolvedWebhookEventRepository;
import com.mailally.email.service.EventNormalizer;
import com.mailally.email.service.WebhookResolverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WebhookResolverServiceImpl implements WebhookResolverService {

    private static final Logger log = LoggerFactory.getLogger(WebhookResolverServiceImpl.class);

    private final EventNormalizer eventNormalizer;
    private final CampaignRecipientLogRepository recipientLogRepository;
    private final com.mailally.campaign.repository.CampaignRecipientRepository campaignRecipientRepository;
    private final EmailRepository emailRepository;
    private final EmailEventRepository emailEventRepository;
    private final UnresolvedWebhookEventRepository unresolvedRepository;
    private final ContactRepository contactRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public WebhookResolverServiceImpl(
            EventNormalizer eventNormalizer,
            CampaignRecipientLogRepository recipientLogRepository,
            com.mailally.campaign.repository.CampaignRecipientRepository campaignRecipientRepository,
            EmailRepository emailRepository,
            EmailEventRepository emailEventRepository,
            UnresolvedWebhookEventRepository unresolvedRepository,
            ContactRepository contactRepository,
            SimpMessagingTemplate messagingTemplate,
            ObjectMapper objectMapper) {
        this.eventNormalizer = eventNormalizer;
        this.recipientLogRepository = recipientLogRepository;
        this.campaignRecipientRepository = campaignRecipientRepository;
        this.emailRepository = emailRepository;
        this.emailEventRepository = emailEventRepository;
        this.unresolvedRepository = unresolvedRepository;
        this.contactRepository = contactRepository;
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public boolean processWebhookEvent(Map<String, Object> payload, String provider) {
        log.info("[WEBHOOK RESOLVER]: Processing incoming {} webhook telemetry payload: {}", provider, payload);

        try {
            EmailEvent event = eventNormalizer.normalizeBrevoWebhook(payload);
            String messageId = event.getProviderMessageId();
            String recipientEmail = payload.get("email") != null ? payload.get("email").toString().trim() : null;
            String subject = payload.get("subject") != null ? payload.get("subject").toString().trim() : null;
            com.mailally.email.entity.Email matchedEmailLog = null;

            // Step 1: Idempotency check if messageId is present
            if (messageId != null && !messageId.isBlank()) {
                if (emailEventRepository.existsByProviderMessageIdAndEventType(messageId, event.getEventType())) {
                    log.info("[WEBHOOK RESOLVER]: Duplicate event skipped for message-id={}, eventType={}", messageId, event.getEventType());
                    return true;
                }
            }

            // Step 2: Strict Identifier Resolution Order
            Optional<CampaignRecipientLog> recipientOpt = Optional.empty();
            Optional<com.mailally.campaign.entity.CampaignRecipient> campaignRecipientOpt = Optional.empty();

            // Order 1: Provider Message ID (raw, cleaned, prefix, containing)
            if (messageId != null && !messageId.isBlank()) {
                recipientOpt = recipientLogRepository.findFirstByProviderMessageId(messageId);
                campaignRecipientOpt = campaignRecipientRepository.findFirstByResponseId(messageId);
                if (recipientOpt.isEmpty() && campaignRecipientOpt.isEmpty()) {
                    String cleanedId = messageId.replaceAll("[<>]", "").trim();
                    recipientOpt = recipientLogRepository.findFirstByProviderMessageId(cleanedId);
                    campaignRecipientOpt = campaignRecipientRepository.findFirstByResponseId(cleanedId);
                    if (recipientOpt.isEmpty() && campaignRecipientOpt.isEmpty()) {
                        recipientOpt = recipientLogRepository.findFirstByProviderMessageIdContaining(cleanedId);
                        campaignRecipientOpt = campaignRecipientRepository.findFirstByResponseIdContaining(cleanedId);
                    }
                    if (recipientOpt.isEmpty() && campaignRecipientOpt.isEmpty() && cleanedId.contains("@")) {
                        String prefix = cleanedId.substring(0, cleanedId.indexOf("@"));
                        recipientOpt = recipientLogRepository.findFirstByProviderMessageIdContaining(prefix);
                        campaignRecipientOpt = campaignRecipientRepository.findFirstByResponseIdContaining(prefix);
                    }
                }
            }

            // Order 2: Recipient Email Address (case-insensitive & trimmed)
            if (recipientOpt.isEmpty() && campaignRecipientOpt.isEmpty() && recipientEmail != null && !recipientEmail.isBlank()) {
                String cleanEmail = recipientEmail.trim();
                campaignRecipientOpt = campaignRecipientRepository.findFirstByContactEmailIgnoreCaseOrderByQueuedAtDesc(cleanEmail);
                if (campaignRecipientOpt.isEmpty()) {
                    recipientOpt = recipientLogRepository.findFirstByEmailIgnoreCaseOrderByCreatedAtDesc(cleanEmail);
                    if (recipientOpt.isEmpty()) {
                        recipientOpt = recipientLogRepository.findFirstByEmailOrderByCreatedAtDesc(cleanEmail);
                    }
                }
            }

            Long resolvedOrgId = null;
            if (recipientOpt.isPresent()) {
                CampaignRecipientLog recipient = recipientOpt.get();
                event.setRecipient(recipient);
                if (recipient.getCampaign() != null) {
                    event.setCampaign(recipient.getCampaign());
                    if (recipient.getCampaign().getOrganization() != null) {
                        resolvedOrgId = recipient.getCampaign().getOrganization().getId();
                    }
                }
                
                // Sync recipient log status using database-level atomic conditional UPDATE
                // This prevents race conditions where e.g. DELIVERED -> PROCESSING could occur
                java.util.List<String> allowedFrom = getAllowedPredecessors(event.getEventType().name());
                if (!allowedFrom.isEmpty()) {
                    int updated = recipientLogRepository.atomicStatusTransition(recipient.getId(), event.getEventType().name(), allowedFrom);
                    if (updated > 0) {
                        log.info("[WEBHOOK RESOLVER]: Atomic status transition to {} for recipient ID={}", event.getEventType().name(), recipient.getId());
                    } else {
                        log.info("[WEBHOOK RESOLVER]: Status transition to {} blocked for recipient ID={} (current status has higher priority)", event.getEventType().name(), recipient.getId());
                    }
                } else {
                    // Terminal/any-source statuses (e.g. COMPLAINT, UNSUBSCRIBED)
                    recipient.setStatus(event.getEventType().name());
                    recipientLogRepository.save(recipient);
                }
                log.info("[WEBHOOK RESOLVER]: Matched recipient log ID={}, campaignId={}, email={}", 
                        recipient.getId(), recipient.getCampaign() != null ? recipient.getCampaign().getId() : "N/A", recipient.getEmail());
            }

            if (campaignRecipientOpt.isPresent()) {
                com.mailally.campaign.entity.CampaignRecipient cr = campaignRecipientOpt.get();
                if (shouldUpdateStatus(cr.getStatus(), event.getEventType().name())) {
                    cr.setStatus(event.getEventType().name());
                }
                LocalDateTime eventTime = event.getOccurredAt() != null ? event.getOccurredAt() : LocalDateTime.now();
                if (event.getEventType() == com.mailally.email.constant.EmailEventType.OPENED && cr.getOpenedAt() == null) {
                    cr.setOpenedAt(eventTime);
                } else if (event.getEventType() == com.mailally.email.constant.EmailEventType.DELIVERED && cr.getDeliveredAt() == null) {
                    cr.setDeliveredAt(eventTime);
                } else if (event.getEventType() == com.mailally.email.constant.EmailEventType.CLICKED && cr.getClickedAt() == null) {
                    cr.setClickedAt(eventTime);
                }
                campaignRecipientRepository.save(cr);
                if (cr.getCampaign() != null) {
                    event.setCampaign(cr.getCampaign());
                    if (cr.getCampaign().getOrganization() != null) {
                        resolvedOrgId = cr.getCampaign().getOrganization().getId();
                    }
                }
                log.info("[WEBHOOK RESOLVER]: Matched CampaignRecipient ID={}, campaignId={}", cr.getId(), cr.getCampaign() != null ? cr.getCampaign().getId() : "N/A");
            }

            // Order 3: Legacy email log fallback for campaign launches that do not create CampaignRecipientLog rows.
            if ((resolvedOrgId == null || event.getCampaign() == null) && recipientEmail != null && !recipientEmail.isBlank()) {
                String cleanEmail = recipientEmail.trim();
                Optional<com.mailally.email.entity.Email> emailOpt =
                        subject != null && !subject.isBlank()
                                ? emailRepository.findFirstByRecipientEmailIgnoreCaseAndSubjectOrderByCreatedAtDesc(cleanEmail, subject)
                                : emailRepository.findFirstByRecipientEmailIgnoreCaseOrderByCreatedAtDesc(cleanEmail);
                if (emailOpt.isPresent()) {
                    matchedEmailLog = emailOpt.get();
                    if (matchedEmailLog.getCampaign() != null) {
                        event.setCampaign(matchedEmailLog.getCampaign());
                    }
                    if (matchedEmailLog.getOrganization() != null) {
                        resolvedOrgId = matchedEmailLog.getOrganization().getId();
                    }
                    log.info("[WEBHOOK RESOLVER]: Resolved via legacy email log ID={}, campaignId={}, email={}",
                            matchedEmailLog.getId(),
                            matchedEmailLog.getCampaign() != null ? matchedEmailLog.getCampaign().getId() : "N/A",
                            matchedEmailLog.getRecipientEmail());

                    // Synthesize missing CampaignRecipientLog if needed so Recipient Activity UI table is populated
                    if (recipientOpt.isEmpty() && matchedEmailLog.getCampaign() != null) {
                        CampaignRecipientLog newRecLog = CampaignRecipientLog.builder()
                                .campaign(matchedEmailLog.getCampaign())
                                .contact(matchedEmailLog.getContact())
                                .email(cleanEmail)
                                .status(event.getEventType().name())
                                .provider(event.getProvider())
                                .providerMessageId(messageId)
                                .createdAt(LocalDateTime.now())
                                .build();
                        recipientOpt = Optional.of(recipientLogRepository.save(newRecLog));
                        event.setRecipient(newRecLog);
                        log.info("[WEBHOOK RESOLVER]: Synthesized missing CampaignRecipientLog ID={} for campaignId={}, email={}",
                                newRecLog.getId(), matchedEmailLog.getCampaign().getId(), cleanEmail);
                    }
                }
            }

            // Fallback Order 4: Contact Email -> Organization ID & Org-wide Recipient Lookup
            if (resolvedOrgId == null && recipientEmail != null && !recipientEmail.isBlank()) {
                String cleanEmail = recipientEmail.trim();
                List<Contact> contacts = contactRepository.findByEmailAndIsDeletedFalse(cleanEmail);
                if (!contacts.isEmpty() && contacts.get(0).getOrganization() != null) {
                    resolvedOrgId = contacts.get(0).getOrganization().getId();
                    log.info("[WEBHOOK RESOLVER]: Resolved organization ID={} via Contact email={}", resolvedOrgId, cleanEmail);

                    // If campaign wasn't linked yet, find latest campaign recipient log in this organization for this email
                    if (event.getCampaign() == null) {
                        Optional<CampaignRecipientLog> matchedOrgLog =
                                recipientLogRepository.findFirstByCampaignOrganizationIdAndEmailIgnoreCaseOrderByCreatedAtDesc(resolvedOrgId, cleanEmail);
                        if (matchedOrgLog.isPresent()) {
                            CampaignRecipientLog rec = matchedOrgLog.get();
                            event.setRecipient(rec);
                            event.setCampaign(rec.getCampaign());
                            rec.setStatus(event.getEventType().name());
                            recipientLogRepository.save(rec);
                            log.info("[WEBHOOK RESOLVER]: Resolved campaign ID={} via org-wide recipient log email={}", rec.getCampaign().getId(), cleanEmail);
                        }
                    }

                    if (event.getCampaign() == null) {
                        Optional<com.mailally.email.entity.Email> orgEmailOpt =
                                subject != null && !subject.isBlank()
                                        ? emailRepository.findFirstByOrganizationIdAndRecipientEmailIgnoreCaseAndSubjectOrderByCreatedAtDesc(resolvedOrgId, cleanEmail, subject)
                                        : emailRepository.findFirstByOrganizationIdAndRecipientEmailIgnoreCaseOrderByCreatedAtDesc(resolvedOrgId, cleanEmail);
                        if (orgEmailOpt.isPresent()) {
                            matchedEmailLog = orgEmailOpt.get();
                            event.setCampaign(matchedEmailLog.getCampaign());
                            log.info("[WEBHOOK RESOLVER]: Resolved campaign ID={} via org legacy email log email={}",
                                    matchedEmailLog.getCampaign() != null ? matchedEmailLog.getCampaign().getId() : "N/A",
                                    cleanEmail);
                        }
                    }
                }
            }

            // Final Guard: campaign analytics require both organization and campaign linkage.
            if (resolvedOrgId == null || event.getCampaign() == null) {
                log.warn("[WEBHOOK RESOLVER]: Could not resolve analytics linkage for message-id={}, email={}, orgId={}, campaignId={}. Pushing to Unresolved Queue.",
                        messageId, recipientEmail, resolvedOrgId, event.getCampaign() != null ? event.getCampaign().getId() : null);
                String jsonPayload = objectMapper.writeValueAsString(payload);
                UnresolvedWebhookEvent unresolved = new UnresolvedWebhookEvent(messageId, recipientEmail, event.getEventType().name(), jsonPayload);
                unresolvedRepository.save(unresolved);
                return false;
            }

            // Save resolved EmailEvent
            event.setOrganizationId(resolvedOrgId);
            emailEventRepository.save(event);
            if (matchedEmailLog != null) {
                applyEventToLegacyEmailLog(matchedEmailLog, event);
                emailRepository.save(matchedEmailLog);
            }
            log.info("[WEBHOOK RESOLVER]: Successfully persisted EmailEvent ID={}, orgId={}, campaignId={}, type={}",
                    event.getId(), event.getOrganizationId(), event.getCampaign() != null ? event.getCampaign().getId() : "N/A", event.getEventType());

            // Push WebSocket Real-Time Telemetry
            try {
                messagingTemplate.convertAndSend("/topic/analytics", (Object) Map.of(
                        "eventType", event.getEventType().name(),
                        "campaignId", event.getCampaign() != null ? event.getCampaign().getId() : "N/A",
                        "email", recipientEmail != null ? recipientEmail : "N/A",
                        "timestamp", LocalDateTime.now().toString()
                ));
            } catch (Exception wsEx) {
                log.warn("[WEBHOOK RESOLVER]: WebSocket notification failed: {}", wsEx.getMessage());
            }

            return true;

        } catch (Exception e) {
            log.error("[WEBHOOK RESOLVER]: Exception processing webhook: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional
    public int retryUnresolvedEvents() {
        List<UnresolvedWebhookEvent> unresolvedList = unresolvedRepository.findByStatusAndRetryCountLessThan("UNRESOLVED", 5);
        if (unresolvedList.isEmpty()) return 0;

        log.info("[WEBHOOK RETRY JOB]: Retrying {} unresolved webhook events...", unresolvedList.size());
        int resolvedCount = 0;

        for (UnresolvedWebhookEvent unresolved : unresolvedList) {
            try {
                unresolved.setRetryCount(unresolved.getRetryCount() + 1);
                Map<String, Object> payload = objectMapper.readValue(unresolved.getPayloadJson(), Map.class);
                
                boolean success = processWebhookEvent(payload, "BREVO_RETRY");
                if (success) {
                    unresolved.setStatus("RESOLVED");
                    resolvedCount++;
                    log.info("[WEBHOOK RETRY JOB]: Successfully resolved pending event ID={}", unresolved.getId());
                } else if (unresolved.getRetryCount() >= 5) {
                    unresolved.setStatus("EXPIRED");
                    unresolved.setLastError("Exceeded maximum 5 resolution retries");
                }
                unresolvedRepository.save(unresolved);
            } catch (Exception ex) {
                log.error("[WEBHOOK RETRY JOB]: Error retrying unresolved event ID={}: {}", unresolved.getId(), ex.getMessage());
                unresolved.setLastError(ex.getMessage());
                unresolvedRepository.save(unresolved);
            }
        }

        return resolvedCount;
    }

    private void applyEventToLegacyEmailLog(com.mailally.email.entity.Email emailLog, EmailEvent event) {
        LocalDateTime eventTime = event.getOccurredAt() != null ? event.getOccurredAt() : LocalDateTime.now();
        switch (event.getEventType()) {
            case DELIVERED -> {
                emailLog.setStatus("DELIVERED");
                if (emailLog.getDeliveredAt() == null) {
                    emailLog.setDeliveredAt(eventTime);
                }
            }
            case OPENED -> {
                emailLog.setStatus("OPENED");
                if (emailLog.getDeliveredAt() == null) {
                    emailLog.setDeliveredAt(eventTime);
                }
                if (emailLog.getOpenedAt() == null) {
                    emailLog.setOpenedAt(eventTime);
                }
            }
            case CLICKED -> {
                emailLog.setStatus("CLICKED");
                if (emailLog.getDeliveredAt() == null) {
                    emailLog.setDeliveredAt(eventTime);
                }
                if (emailLog.getOpenedAt() == null) {
                    emailLog.setOpenedAt(eventTime);
                }
                if (emailLog.getClickedAt() == null) {
                    emailLog.setClickedAt(eventTime);
                }
            }
            case BOUNCED -> {
                emailLog.setStatus("BOUNCED");
                emailLog.setBouncedAt(eventTime);
            }
            case COMPLAINT -> emailLog.setStatus("COMPLAINT");
            case UNSUBSCRIBED -> emailLog.setStatus("UNSUBSCRIBED");
            default -> {
            }
        }
    }

    private boolean shouldUpdateStatus(String currentStatus, String newStatus) {
        if (currentStatus == null) return true;
        if (currentStatus.equalsIgnoreCase(newStatus)) return true;
        int currentPriority = getStatusPriority(currentStatus);
        int newPriority = getStatusPriority(newStatus);
        return newPriority >= currentPriority;
    }

    private int getStatusPriority(String status) {
        if (status == null) return 0;
        switch (status.toUpperCase()) {
            case "QUEUED": return 1;
            case "PROCESSING": return 2;
            case "ACCEPTED": return 3;
            case "SENT": return 3;
            case "DELIVERED": return 4;
            case "BOUNCED": return 5;
            case "HARD_BOUNCE": return 5;
            case "SOFT_BOUNCE": return 5;
            case "FAILED": return 5;
            case "INVALID": return 5;
            case "REJECTED": return 5;
            case "OPENED": return 6;
            case "CLICKED": return 7;
            default: return 3;
        }
    }

    /**
     * Returns the list of statuses from which a transition to targetStatus is allowed.
     * Empty list means the status can be set from any state (e.g. COMPLAINT, UNSUBSCRIBED).
     */
    private java.util.List<String> getAllowedPredecessors(String targetStatus) {
        if (targetStatus == null) return java.util.List.of();
        switch (targetStatus.toUpperCase()) {
            case "PROCESSING":
                return java.util.List.of("QUEUED");
            case "ACCEPTED":
            case "SENT":
                return java.util.List.of("QUEUED", "PROCESSING");
            case "DELIVERED":
                return java.util.List.of("QUEUED", "PROCESSING", "ACCEPTED", "SENT");
            case "OPENED":
                return java.util.List.of("QUEUED", "PROCESSING", "ACCEPTED", "SENT", "DELIVERED");
            case "CLICKED":
                return java.util.List.of("QUEUED", "PROCESSING", "ACCEPTED", "SENT", "DELIVERED", "OPENED");
            case "BOUNCED":
            case "HARD_BOUNCE":
            case "SOFT_BOUNCE":
            case "FAILED":
            case "REJECTED":
                return java.util.List.of("QUEUED", "PROCESSING", "ACCEPTED", "SENT");
            default:
                // COMPLAINT, UNSUBSCRIBED, INVALID: can be set from any state
                return java.util.List.of();
        }
    }
}
