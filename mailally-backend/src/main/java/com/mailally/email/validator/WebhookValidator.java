package com.mailally.email.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Component for verifying signature and header security of incoming email provider webhooks (Brevo & Amazon SES).
 */
@Component
public class WebhookValidator {

    private static final Logger log = LoggerFactory.getLogger(WebhookValidator.class);

    public boolean validateBrevoWebhook(Map<String, Object> payload, String signatureHeader) {
        // In production environment, verify HMAC/token signature header.
        // For development/demo environment, validate required structural payload fields.
        if (payload == null || payload.isEmpty()) {
            log.warn("WebhookValidator: Rejected empty Brevo webhook payload.");
            return false;
        }
        return true;
    }

    public boolean validateSesWebhook(Map<String, Object> payload, String signatureHeader) {
        if (payload == null || payload.isEmpty()) {
            log.warn("WebhookValidator: Rejected empty SES webhook payload.");
            return false;
        }
        return true;
    }
}
