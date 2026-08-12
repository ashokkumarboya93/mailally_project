package com.mailally.email.provider;

/**
 * Interface defining contract for email provider integrations.
 */
public interface EmailProvider {

    /**
     * Sends an email via the provider.
     */
    EmailSendResult send(String to, String toName, String from, String fromName, String replyTo, String subject, String htmlBody);

    /**
     * Unique identifier for the provider (SMTP, BREVO, SES).
     */
    String getProviderName();

    /**
     * Checks if provider is enabled and configured.
     */
    boolean isAvailable();

    /**
     * Performs a health check check on this provider (latency, connection).
     */
    com.mailally.email.dto.ProviderHealthDto health();

    /**
     * Returns current sending quota metrics (e.g. limit per second).
     */
    int quota();

    /**
     * Returns optimal sending batch size.
     */
    int batch();

    /**
     * Sends a batch of personalized messages via provider's batch API.
     */
    default BatchSendResult sendBatch(java.util.List<RecipientBatchItem> items, String from, String fromName, String replyTo, String defaultSubject, String defaultHtmlBody, String idempotencyKey) {
        throw new UnsupportedOperationException("Batch sending not supported by provider " + getProviderName());
    }

    /**
     * Flags indicating capabilities.
     */
    boolean supportsBulk();
    boolean supportsWebhook();
    boolean supportsTracking();
}
