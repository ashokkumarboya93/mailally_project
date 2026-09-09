package com.mailally.email.provider;

import com.mailally.email.config.EmailEngineConfig;
import com.mailally.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory class for selecting and managing email provider strategies with failover support.
 */
@Component
public class EmailProviderFactory {

    private static final Logger log = LoggerFactory.getLogger(EmailProviderFactory.class);

    private final Map<String, EmailProvider> providerMap;
    private final EmailEngineConfig config;

    public EmailProviderFactory(List<EmailProvider> providers, EmailEngineConfig config) {
        this.providerMap = providers.stream()
                .collect(Collectors.toMap(
                        p -> p.getProviderName().toUpperCase(),
                        Function.identity()
                ));
        this.config = config;
    }

    /**
     * Resolves the primary active provider configured in application properties.
     */
    public EmailProvider getActiveProvider() {
        String active = config.getActiveProvider();
        if (active == null || active.isBlank()) {
            active = SmtpEmailProvider.PROVIDER_NAME;
        }
        EmailProvider provider = providerMap.get(active.toUpperCase());
        if (provider == null) {
            log.warn("Requested provider '{}' not available. Defaulting to SMTP.", active);
            provider = providerMap.get(SmtpEmailProvider.PROVIDER_NAME);
            if (provider == null) {
                provider = providerMap.get(BrevoEmailProvider.PROVIDER_NAME);
            }
        }
        return provider;
    }

    /**
     * Retrieves a provider by name.
     */
    public EmailProvider getProvider(String providerName) {
        if (providerName == null || providerName.isBlank()) {
            return getActiveProvider();
        }
        EmailProvider provider = providerMap.get(providerName.toUpperCase());
        if (provider == null) {
            throw new CustomException("Email provider '" + providerName + "' not supported");
        }
        return provider;
    }

    /**
     * Dispatches an email using the active provider, with automatic failover to available backup providers if the primary fails.
     */
    public EmailSendResult sendWithFailover(String to, String toName, String from, String fromName, String replyTo, String subject, String htmlBody) {
        EmailProvider primary = getActiveProvider();
        long startTime = System.currentTimeMillis();

        try {
            log.info("Sending email to {} using primary provider: {}", to, primary.getProviderName());
            EmailSendResult result = primary.send(to, toName, from, fromName, replyTo, subject, htmlBody);
            if (result.isSuccess()) {
                log.info("Email delivered to {} via {} in {}ms", to, primary.getProviderName(), (System.currentTimeMillis() - startTime));
                return result;
            }
            log.warn("Primary provider {} failed for recipient {}. Error: {}. Attempting failover...",
                    primary.getProviderName(), to, result.getErrorMessage());
        } catch (Exception ex) {
            log.warn("Primary provider {} threw exception for recipient {}: {}. Attempting failover...",
                    primary.getProviderName(), to, ex.getMessage());
        }

        // Attempt failover to other registered and available providers
        for (EmailProvider backup : providerMap.values()) {
            if (backup.getProviderName().equalsIgnoreCase(primary.getProviderName())) {
                continue;
            }
            if (backup.isAvailable()) {
                long backupStart = System.currentTimeMillis();
                try {
                    log.info("Failover: Attempting send via provider {}", backup.getProviderName());
                    EmailSendResult failoverResult = backup.send(to, toName, from, fromName, replyTo, subject, htmlBody);
                    if (failoverResult.isSuccess()) {
                        log.info("Failover successful: Email delivered to {} via {} in {}ms", to, backup.getProviderName(), (System.currentTimeMillis() - backupStart));
                        return failoverResult;
                    }
                } catch (Exception fEx) {
                    log.warn("Failover provider {} threw exception for recipient {}: {}", backup.getProviderName(), to, fEx.getMessage());
                }
            }
        }

        log.error("All email providers (primary + failovers) failed for recipient: {}", to);
        return EmailSendResult.fail("All email providers failed to dispatch email to " + to, primary.getProviderName());
    }

    /**
     * Dispatches a batch of emails using the active provider's batch API with failover support.
     */
    public BatchSendResult sendBatchWithFailover(List<RecipientBatchItem> items, String from, String fromName, String replyTo, String defaultSubject, String defaultHtmlBody, String idempotencyKey) {
        if (items == null || items.isEmpty()) {
            return BatchSendResult.fail("Empty batch items", "NONE", "400");
        }

        EmailProvider primary = getActiveProvider();
        if (primary.isAvailable() && primary.supportsBulk()) {
            log.info("Dispatching batch of {} items via primary provider: {}", items.size(), primary.getProviderName());
            BatchSendResult result = primary.sendBatch(items, from, fromName, replyTo, defaultSubject, defaultHtmlBody, idempotencyKey);
            if (result.isSuccess()) {
                return result;
            }
            log.warn("Primary provider {} batch send failed: {}. Attempting failover...", primary.getProviderName(), result.getErrorMessage());
        }

        // Attempt failover to backup bulk-capable providers
        for (EmailProvider backup : providerMap.values()) {
            if (backup.getProviderName().equalsIgnoreCase(primary.getProviderName())) {
                continue;
            }
            if (backup.isAvailable() && backup.supportsBulk()) {
                log.info("Failover batch send via provider {}", backup.getProviderName());
                BatchSendResult failoverResult = backup.sendBatch(items, from, fromName, replyTo, defaultSubject, defaultHtmlBody, idempotencyKey);
                if (failoverResult.isSuccess()) {
                    return failoverResult;
                }
            }
        }

        // Fallback: Individual item dispatches
        log.warn("Bulk batch dispatch unsupported or failed across all providers. Executing individual item dispatches...");
        Map<Long, String> recipientMsgMap = new java.util.HashMap<>();
        int successCount = 0;
        String lastError = "Batch dispatch failed across all providers";
        for (RecipientBatchItem item : items) {
            String sub = (item.getPersonalizedSubject() != null && !item.getPersonalizedSubject().isBlank()) ? item.getPersonalizedSubject() : defaultSubject;
            String html = (item.getPersonalizedHtml() != null && !item.getPersonalizedHtml().isBlank()) ? item.getPersonalizedHtml() : defaultHtmlBody;
            EmailSendResult singleRes = sendWithFailover(item.getEmail(), item.getFirstName(), from, fromName, replyTo, sub, html);
            if (singleRes.isSuccess()) {
                successCount++;
                if (item.getRecipientLogId() != null) {
                    recipientMsgMap.put(item.getRecipientLogId(), singleRes.getResponseId());
                }
            } else {
                lastError = singleRes.getErrorMessage();
            }
        }

        if (successCount > 0) {
            return BatchSendResult.ok("FALLBACK-BATCH-" + System.currentTimeMillis(), recipientMsgMap, primary.getProviderName());
        } else {
            return BatchSendResult.fail(lastError, primary.getProviderName(), "500");
        }
    }

    public Map<String, EmailProvider> getAllProviders() {
        return providerMap;
    }
}
