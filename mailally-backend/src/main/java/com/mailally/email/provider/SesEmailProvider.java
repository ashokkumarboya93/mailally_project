package com.mailally.email.provider;

import com.mailally.email.config.EmailEngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.SesV2AsyncClient;
import software.amazon.awssdk.services.sesv2.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Production-ready Amazon SES v2 provider implementation of {@link EmailProvider}.
 * Sends transactional and bulk campaign emails using AWS SES v2 SDK.
 */
@Component
public class SesEmailProvider implements EmailProvider {

    private static final Logger log = LoggerFactory.getLogger(SesEmailProvider.class);
    public static final String PROVIDER_NAME = "SES";

    private final EmailEngineConfig config;
    private volatile SesV2Client cachedClient;
    private volatile SesV2AsyncClient cachedAsyncClient;

    public SesEmailProvider(EmailEngineConfig config) {
        this.config = config;
    }

    private SesV2Client getClient() {
        if (cachedClient == null) {
            synchronized (this) {
                if (cachedClient == null) {
                    EmailEngineConfig.SesConfig sesConfig = config.getSes();
                    String regionStr = (sesConfig.getRegion() != null && !sesConfig.getRegion().isBlank()) 
                            ? sesConfig.getRegion() 
                            : "us-east-1";

                    AwsBasicCredentials credentials = AwsBasicCredentials.create(
                            sesConfig.getAccessKey().trim(),
                            sesConfig.getSecretKey().trim()
                    );

                    cachedClient = SesV2Client.builder()
                            .region(Region.of(regionStr.trim()))
                            .credentialsProvider(StaticCredentialsProvider.create(credentials))
                            .build();
                }
            }
        }
        return cachedClient;
    }

    private SesV2AsyncClient getAsyncClient() {
        if (cachedAsyncClient == null) {
            synchronized (this) {
                if (cachedAsyncClient == null) {
                    EmailEngineConfig.SesConfig sesConfig = config.getSes();
                    String regionStr = (sesConfig.getRegion() != null && !sesConfig.getRegion().isBlank()) 
                            ? sesConfig.getRegion() 
                            : "us-east-1";

                    AwsBasicCredentials credentials = AwsBasicCredentials.create(
                            sesConfig.getAccessKey().trim(),
                            sesConfig.getSecretKey().trim()
                    );

                    cachedAsyncClient = SesV2AsyncClient.builder()
                            .region(Region.of(regionStr.trim()))
                            .credentialsProvider(StaticCredentialsProvider.create(credentials))
                            .build();
                }
            }
        }
        return cachedAsyncClient;
    }

    @Override
    public EmailSendResult send(String to, String toName, String from, String fromName, String replyTo, String subject, String htmlBody) {
        if (!isAvailable()) {
            log.warn("Attempted to send via Amazon SES provider, but SES credentials are not configured.");
            return EmailSendResult.fail("Amazon SES Credentials (Access Key / Secret Key) not configured", PROVIDER_NAME);
        }

        try {
            SesV2Client client = getClient();
            String senderEmail = (from != null && !from.isBlank()) ? from : config.getDefaultSenderEmail();
            String senderName = (fromName != null && !fromName.isBlank()) ? fromName : config.getDefaultSenderName();
            
            String formattedFrom = (senderName != null && !senderName.isBlank())
                    ? String.format("%s <%s>", senderName, senderEmail)
                    : senderEmail;

            Destination destination = Destination.builder()
                    .toAddresses(to)
                    .build();

            Content subContent = Content.builder()
                    .data(subject != null ? subject : "")
                    .charset("UTF-8")
                    .build();

            Content htmlContent = Content.builder()
                    .data(htmlBody != null ? htmlBody : "")
                    .charset("UTF-8")
                    .build();

            Body body = Body.builder()
                    .html(htmlContent)
                    .build();

            Message message = Message.builder()
                    .subject(subContent)
                    .body(body)
                    .build();

            EmailContent emailContent = EmailContent.builder()
                    .simple(message)
                    .build();

            SendEmailRequest.Builder requestBuilder = SendEmailRequest.builder()
                    .fromEmailAddress(formattedFrom)
                    .destination(destination)
                    .content(emailContent);

            if (replyTo != null && !replyTo.isBlank()) {
                requestBuilder.replyToAddresses(replyTo);
            }

            SendEmailResponse response = client.sendEmail(requestBuilder.build());
            String messageId = "SES-" + response.messageId();

            log.info("Successfully sent email via Amazon SES to {} [Message ID: {}]", to, messageId);
            return EmailSendResult.ok(messageId, PROVIDER_NAME);

        } catch (Exception ex) {
            log.error("Failed to send email via Amazon SES to {}: {}", to, ex.getMessage(), ex);
            return EmailSendResult.fail("Amazon SES Exception: " + ex.getMessage(), PROVIDER_NAME);
        }
    }

    @Override
    public BatchSendResult sendBatch(List<RecipientBatchItem> items, String from, String fromName, String replyTo, String defaultSubject, String defaultHtmlBody, String idempotencyKey) {
        if (!isAvailable()) {
            return BatchSendResult.fail("Amazon SES Credentials not configured", PROVIDER_NAME, "401");
        }

        if (items == null || items.isEmpty()) {
            return BatchSendResult.fail("Empty recipient items batch", PROVIDER_NAME, "400");
        }

        Map<Long, String> recipientMsgMap = new java.util.concurrent.ConcurrentHashMap<>();
        java.util.concurrent.atomic.AtomicInteger successCount = new java.util.concurrent.atomic.AtomicInteger(0);
        java.util.concurrent.atomic.AtomicInteger failureCount = new java.util.concurrent.atomic.AtomicInteger(0);
        java.util.concurrent.atomic.AtomicReference<String> lastError = new java.util.concurrent.atomic.AtomicReference<>(null);

        // Process batch concurrently across parallel stream / futures using cached client connection pool
        items.parallelStream().forEach(item -> {
            String itemSubject = (item.getPersonalizedSubject() != null && !item.getPersonalizedSubject().isBlank())
                    ? item.getPersonalizedSubject()
                    : defaultSubject;
            String itemHtml = (item.getPersonalizedHtml() != null && !item.getPersonalizedHtml().isBlank())
                    ? item.getPersonalizedHtml()
                    : defaultHtmlBody;

            EmailSendResult singleResult = send(item.getEmail(), item.getFirstName(), from, fromName, replyTo, itemSubject, itemHtml);
            if (singleResult.isSuccess()) {
                if (item.getRecipientLogId() != null) {
                    recipientMsgMap.put(item.getRecipientLogId(), singleResult.getResponseId());
                }
                successCount.incrementAndGet();
            } else {
                failureCount.incrementAndGet();
                lastError.set(singleResult.getErrorMessage());
            }
        });

        if (successCount.get() > 0) {
            String batchMessageId = "SES-BATCH-" + System.currentTimeMillis();
            log.info("Amazon SES Concurrent Batch complete [Success: {}, Failed: {}]", successCount.get(), failureCount.get());
            return BatchSendResult.ok(batchMessageId, recipientMsgMap, PROVIDER_NAME);
        } else {
            return BatchSendResult.fail("SES Batch dispatch failed for all recipients: " + lastError.get(), PROVIDER_NAME, "500");
        }
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public boolean isAvailable() {
        return config != null 
                && config.getSes() != null 
                && config.getSes().isConfigured()
                && config.getSes().getSecretKey() != null
                && !config.getSes().getSecretKey().isBlank()
                && !"your-ses-secret-key".equals(config.getSes().getSecretKey());
    }

    @Override
    public com.mailally.email.dto.ProviderHealthDto health() {
        return com.mailally.email.dto.ProviderHealthDto.builder()
                .providerName(PROVIDER_NAME)
                .available(isAvailable())
                .active(true)
                .statusMessage(isAvailable() ? "Amazon SES Provider SDK Healthy" : "Amazon SES Credentials Missing / Unconfigured")
                .build();
    }

    @Override
    public int quota() {
        return 14; // 14 sends per second (default SES rate limit)
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

