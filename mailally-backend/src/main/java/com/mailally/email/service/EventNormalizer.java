package com.mailally.email.service;

import com.mailally.email.constant.EmailEventType;
import com.mailally.email.entity.EmailEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Service responsible for mapping raw vendor webhook JSON payloads (Brevo, Amazon SES)
 * into normalized EmailEvent entity domain models.
 */
@Service
public class EventNormalizer {

    public EmailEvent normalizeBrevoWebhook(Map<String, Object> payload) {
        String brevoEvent = valueAsString(payload, "event", "Event", "type", "event_type");
        String messageId = valueAsString(payload, "message-id", "messageId", "message_id", "MessageID", "MessageId", "uuid");
        String userAgent = valueAsString(payload, "user-agent", "user_agent", "userAgent");
        String ipAddress = valueAsString(payload, "ip", "ip_address", "ipAddress");

        EmailEventType eventType = EmailEventType.fromString(brevoEvent != null ? brevoEvent : "SENT");

        return EmailEvent.builder()
                .eventType(eventType)
                .provider("BREVO")
                .providerMessageId(messageId)
                .userAgent(userAgent)
                .ipAddress(ipAddress)
                .metadata(payload.toString())
                .timestamp(LocalDateTime.now())
                .occurredAt(LocalDateTime.now())
                .build();
    }

    private String valueAsString(Map<String, Object> payload, String... keys) {
        for (String key : keys) {
            Object value = payload.get(key);
            if (value != null && !value.toString().isBlank()) {
                return value.toString().trim();
            }
        }
        return null;
    }

    public EmailEvent normalizeSesWebhook(Map<String, Object> payload) {
        String notificationType = payload.get("notificationType") != null ? payload.get("notificationType").toString() : "SENT";
        EmailEventType eventType = EmailEventType.fromString(notificationType);

        return EmailEvent.builder()
                .eventType(eventType)
                .provider("SES")
                .metadata(payload.toString())
                .timestamp(LocalDateTime.now())
                .occurredAt(LocalDateTime.now())
                .build();
    }
}
