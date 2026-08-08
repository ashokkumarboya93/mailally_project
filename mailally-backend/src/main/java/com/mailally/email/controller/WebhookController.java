package com.mailally.email.controller;

import com.mailally.common.response.ApiResponse;
import com.mailally.email.constant.EmailEventType;
import com.mailally.email.entity.CampaignRecipientLog;
import com.mailally.email.entity.EmailEvent;
import com.mailally.email.event.KafkaEventPublisher;
import com.mailally.email.repository.CampaignRecipientLogRepository;
import com.mailally.email.repository.EmailEventRepository;
import com.mailally.email.service.EventNormalizer;
import com.mailally.email.service.WebhookResolverService;
import com.mailally.email.validator.WebhookValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controller receiving provider webhook delivery/engagement events, mapping them, enforcing idempotency,
 * persisting EmailEvent records, and broadcasting updates via WebSockets.
 */
@RestController
@RequestMapping("/api/v2/webhooks")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

    private final KafkaEventPublisher eventPublisher;
    private final WebhookValidator webhookValidator;
    private final WebhookResolverService webhookResolverService;
    private final EventNormalizer eventNormalizer;
    private final EmailEventRepository emailEventRepository;
    private final CampaignRecipientLogRepository recipientLogRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public WebhookController(KafkaEventPublisher eventPublisher,
                             WebhookValidator webhookValidator,
                             WebhookResolverService webhookResolverService,
                             EventNormalizer eventNormalizer,
                             EmailEventRepository emailEventRepository,
                             CampaignRecipientLogRepository recipientLogRepository,
                             SimpMessagingTemplate messagingTemplate) {
        this.eventPublisher = eventPublisher;
        this.webhookValidator = webhookValidator;
        this.webhookResolverService = webhookResolverService;
        this.eventNormalizer = eventNormalizer;
        this.emailEventRepository = emailEventRepository;
        this.recipientLogRepository = recipientLogRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Health check endpoint for Brevo Webhook URL verification.
     */
    @GetMapping("/brevo")
    public ResponseEntity<ApiResponse<String>> checkBrevoWebhookHealth() {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("MailAlly Brevo Webhook Endpoint is Active & Healthy (Listening for HTTP POST events)")
                .data("OK")
                .timestamp(LocalDateTime.now())
                .build());
    }

    /**
     * Endpoint for Brevo Webhook Integration.
     */
    @PostMapping("/brevo")
    public ResponseEntity<ApiResponse<Void>> receiveBrevoWebhook(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "X-Brevo-Signature", required = false) String signatureHeader) {
        log.info("WebhookController [BREVO]: Received webhook notification payload: {}", payload);

        if (!webhookValidator.validateBrevoWebhook(payload, signatureHeader)) {
            log.warn("WebhookController [BREVO]: Signature check failed. Rejecting request.");
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .success(false).message("Invalid Brevo webhook signature").timestamp(LocalDateTime.now()).build());
        }

        try {
            boolean processed = webhookResolverService.processWebhookEvent(payload, "BREVO");
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .success(true)
                    .message(processed ? "Brevo webhook event processed and resolved" : "Brevo webhook event queued for async retry resolution")
                    .timestamp(LocalDateTime.now())
                    .build());
        } catch (Exception e) {
            log.error("WebhookController [BREVO]: Error processing webhook event: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ApiResponse.<Void>builder()
                    .success(false).message("Error processing Brevo webhook: " + e.getMessage()).timestamp(LocalDateTime.now()).build());
        }
    }

    /**
     * Endpoint for Amazon SES Webhook Integration.
     */
    @PostMapping("/ses")
    public ResponseEntity<ApiResponse<Void>> receiveSesWebhook(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "X-Amz-Sns-Signature", required = false) String signatureHeader) {
        log.info("WebhookController [SES]: Received webhook notification payload: {}", payload);

        if (!webhookValidator.validateSesWebhook(payload, signatureHeader)) {
            log.warn("WebhookController [SES]: Signature check failed. Rejecting request.");
            return ResponseEntity.badRequest().body(ApiResponse.<Void>builder()
                    .success(false).message("Invalid SES webhook signature").timestamp(LocalDateTime.now()).build());
        }

        try {
            EmailEvent event = eventNormalizer.normalizeSesWebhook(payload);
            String messageId = event.getProviderMessageId();

            if (messageId != null && !messageId.isBlank()) {
                if (emailEventRepository.existsByProviderMessageIdAndEventType(messageId, event.getEventType())) {
                    log.info("WebhookController [SES]: Duplicate event detected for message-id={}, eventType={}. Skipping persistence.", messageId, event.getEventType());
                    return ResponseEntity.ok(ApiResponse.<Void>builder().success(true).message("Duplicate event skipped").timestamp(LocalDateTime.now()).build());
                }

                Optional<CampaignRecipientLog> recipientOpt = recipientLogRepository.findFirstByProviderMessageId(messageId);
                if (recipientOpt.isPresent()) {
                    CampaignRecipientLog recipient = recipientOpt.get();
                    event.setRecipient(recipient);
                    event.setCampaign(recipient.getCampaign());
                    if (recipient.getCampaign() != null && recipient.getCampaign().getOrganization() != null) {
                        event.setOrganizationId(recipient.getCampaign().getOrganization().getId());
                    }
                    recipient.setStatus(event.getEventType().name());
                    recipientLogRepository.save(recipient);
                }
            }

            emailEventRepository.save(event);
            log.info("WebhookController [SES]: Saved EmailEvent record. Type={}", event.getEventType());

            broadcastUpdate(event);
            eventPublisher.publishEmailDeliveryEvent(null, null, null, event.getEventType().name(), "SES", null);

        } catch (Exception e) {
            log.error("WebhookController [SES]: Error processing SES webhook event: {}", e.getMessage(), e);
        }

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true).message("SES webhook event processed").timestamp(LocalDateTime.now()).build());
    }

    private void broadcastUpdate(EmailEvent event) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("campaignId", event.getCampaign() != null ? event.getCampaign().getId() : null);
            message.put("eventType", event.getEventType().name());
            message.put("provider", event.getProvider());
            message.put("email", event.getRecipient() != null ? event.getRecipient().getEmail() : "unknown@domain.com");
            message.put("timestamp", event.getTimestamp().toString());

            messagingTemplate.convertAndSend("/topic/analytics", (Object) message);
            log.info("WebhookController: Dispatched WebSocket telemetry push for eventType={}", event.getEventType());
        } catch (Exception e) {
            log.warn("WebhookController: Failed to push WebSocket telemetry update: {}", e.getMessage());
        }
    }
}
