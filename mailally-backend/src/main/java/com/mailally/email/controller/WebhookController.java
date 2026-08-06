package com.mailally.email.controller;

import com.mailally.common.response.ApiResponse;
import com.mailally.email.event.KafkaEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controller receiving provider webhook delivery/engagement events and publishing them to Kafka.
 */
@RestController
@RequestMapping("/api/v2/webhooks")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

    private final KafkaEventPublisher eventPublisher;

    public WebhookController(KafkaEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * Endpoint for Brevo Webhook Integration.
     */
    @PostMapping("/brevo")
    public ResponseEntity<ApiResponse<Void>> receiveBrevoWebhook(@RequestBody Map<String, Object> payload) {
        log.info("WebhookController: Received webhook request from Brevo: {}", payload);

        try {
            String email = (String) payload.get("email");
            String brevoEvent = (String) payload.get("event");
            String messageId = (String) payload.get("message-id");

            // Normalize Brevo events to standard lifecycle states
            String normalizedStatus = "SENT";
            if ("opened".equalsIgnoreCase(brevoEvent)) normalizedStatus = "OPENED";
            else if ("click".equalsIgnoreCase(brevoEvent)) normalizedStatus = "CLICKED";
            else if ("delivered".equalsIgnoreCase(brevoEvent)) normalizedStatus = "DELIVERED";
            else if (brevoEvent != null && brevoEvent.contains("bounce")) normalizedStatus = "BOUNCED";

            log.info("WebhookController: Normalized Brevo event status -> email={}, event={}, status={}",
                    email, brevoEvent, normalizedStatus);

            // Publish event to Kafka
            eventPublisher.publishEmailDeliveryEvent(null, null, email, normalizedStatus, "BREVO", null);

        } catch (Exception e) {
            log.error("WebhookController: Failed to process Brevo webhook event: {}", e.getMessage());
        }

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true).message("Brevo webhook event processed").timestamp(LocalDateTime.now()).build());
    }

    /**
     * Endpoint for Amazon SES Webhook Integration.
     */
    @PostMapping("/ses")
    public ResponseEntity<ApiResponse<Void>> receiveSesWebhook(@RequestBody Map<String, Object> payload) {
        log.info("WebhookController: Received webhook request from Amazon SES: {}", payload);

        try {
            String notificationType = (String) payload.get("notificationType");
            
            if ("Bounce".equalsIgnoreCase(notificationType)) {
                Map<String, Object> bounce = (Map<String, Object>) payload.get("bounce");
                if (bounce != null) {
                    log.warn("WebhookController: SES Bounce detected: {}", bounce);
                    eventPublisher.publishEmailDeliveryEvent(null, null, null, "BOUNCED", "SES", "SES Bounce notification");
                }
            } else if ("Delivery".equalsIgnoreCase(notificationType)) {
                Map<String, Object> delivery = (Map<String, Object>) payload.get("delivery");
                if (delivery != null) {
                    log.info("WebhookController: SES Delivery detected: {}", delivery);
                    eventPublisher.publishEmailDeliveryEvent(null, null, null, "DELIVERED", "SES", null);
                }
            }
        } catch (Exception e) {
            log.error("WebhookController: Failed to process SES webhook event: {}", e.getMessage());
        }

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true).message("SES webhook event processed").timestamp(LocalDateTime.now()).build());
    }
}
