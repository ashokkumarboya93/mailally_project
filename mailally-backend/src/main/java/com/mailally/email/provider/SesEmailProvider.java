package com.mailally.email.provider;

import com.mailally.email.config.EmailEngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Amazon SES stub adapter implementing {@link EmailProvider}.
 * Provides architecture placeholder for future AWS SES SDK/API integration.
 */
@Component
public class SesEmailProvider implements EmailProvider {

    private static final Logger log = LoggerFactory.getLogger(SesEmailProvider.class);
    public static final String PROVIDER_NAME = "SES";

    private final EmailEngineConfig config;

    public SesEmailProvider(EmailEngineConfig config) {
        this.config = config;
    }

    @Override
    public EmailSendResult send(String to, String toName, String from, String fromName, String replyTo, String subject, String htmlBody) {
        if (!isAvailable()) {
            log.warn("Attempted to send via Amazon SES provider, but Access Key is not configured.");
            return EmailSendResult.fail("Amazon SES Access Key not configured", PROVIDER_NAME);
        }
        // Stub adapter logic for future AWS SES integration
        log.info("Amazon SES provider stub invoked for recipient: {}", to);
        return EmailSendResult.fail("Amazon SES provider integration is currently in stub mode", PROVIDER_NAME);
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public boolean isAvailable() {
        return config != null && config.getSes() != null && config.getSes().isConfigured();
    }

    @Override
    public com.mailally.email.dto.ProviderHealthDto health() {
        return com.mailally.email.dto.ProviderHealthDto.builder()
                .providerName(PROVIDER_NAME)
                .available(isAvailable())
                .active(true)
                .statusMessage(isAvailable() ? "Amazon SES Provider Ready" : "Amazon SES Stub Mode / Unconfigured")
                .build();
    }

    @Override
    public int quota() {
        return 14; // 14 sends per second (default SES sandbox rate limit)
    }

    @Override
    public int batch() {
        return 200; // Optimal chunk size for AWS SES
    }

    @Override
    public boolean supportsBulk() {
        return true;
    }

    @Override
    public boolean supportsWebhook() {
        return true;
    }

    @Override
    public boolean supportsTracking() {
        return true;
    }
}
