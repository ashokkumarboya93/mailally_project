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
            throw new CustomException("Email provider '" + active + "' not supported. Available: " + providerMap.keySet());
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
        log.info("Sending email to {} using primary provider: {}", to, primary.getProviderName());

        EmailSendResult result = primary.send(to, toName, from, fromName, replyTo, subject, htmlBody);
        if (result.isSuccess()) {
            return result;
        }

        log.warn("Primary provider {} failed for recipient {}. Error: {}. Attempting failover...",
                primary.getProviderName(), to, result.getErrorMessage());

        // Attempt failover to other registered and available providers
        for (EmailProvider backup : providerMap.values()) {
            if (backup.getProviderName().equalsIgnoreCase(primary.getProviderName())) {
                continue; // Skip primary already attempted
            }
            if (backup.isAvailable()) {
                log.info("Failover: Attempting send via provider {}", backup.getProviderName());
                EmailSendResult failoverResult = backup.send(to, toName, from, fromName, replyTo, subject, htmlBody);
                if (failoverResult.isSuccess()) {
                    return failoverResult;
                }
            }
        }

        log.error("All email providers (primary + failovers) failed for recipient: {}", to);
        return result; // Return original failure result if all fail
    }

    public Map<String, EmailProvider> getAllProviders() {
        return providerMap;
    }
}
