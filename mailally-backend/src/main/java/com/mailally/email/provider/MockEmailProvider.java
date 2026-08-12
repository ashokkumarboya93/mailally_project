package com.mailally.email.provider;

import com.mailally.email.dto.ProviderHealthDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Mock Email Provider for high-scale performance testing (e.g. 5,000 to 50,000 recipients)
 * without consuming live Brevo API quota during local development.
 */
@Component
public class MockEmailProvider implements EmailProvider {

    private static final Logger log = LoggerFactory.getLogger(MockEmailProvider.class);
    public static final String PROVIDER_NAME = "MOCK";

    @Override
    public EmailSendResult send(String to, String toName, String from, String fromName, String replyTo, String subject, String htmlBody) {
        String msgId = "MOCK-MSG-" + UUID.randomUUID().toString().substring(0, 8);
        return EmailSendResult.ok(msgId, PROVIDER_NAME);
    }

    @Override
    public BatchSendResult sendBatch(List<RecipientBatchItem> items, String from, String fromName, String replyTo, String defaultSubject, String defaultHtmlBody, String idempotencyKey) {
        if (items == null || items.isEmpty()) {
            return BatchSendResult.fail("Empty batch items", PROVIDER_NAME, "400");
        }

        String batchId = "MOCK-BATCH-" + UUID.randomUUID().toString().substring(0, 8);
        Map<Long, String> recipientMsgMap = new HashMap<>();

        for (RecipientBatchItem item : items) {
            String msgId = "MOCK-" + item.getRecipientLogId() + "-" + UUID.randomUUID().toString().substring(0, 6);
            recipientMsgMap.put(item.getRecipientLogId(), msgId);
        }

        try {
            // Simulate 50ms realistic network latency per batch
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("[MOCK EMAIL PROVIDER]: Dispatched mock batch items={}, batchId={}, idempotencyKey={}", items.size(), batchId, idempotencyKey);
        return BatchSendResult.ok(batchId, recipientMsgMap, PROVIDER_NAME);
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public ProviderHealthDto health() {
        return ProviderHealthDto.builder()
                .providerName(PROVIDER_NAME)
                .available(true)
                .active(true)
                .statusMessage("Mock Email Provider Healthy for High-Scale Testing")
                .build();
    }

    @Override
    public int quota() {
        return 1000;
    }

    @Override
    public int batch() {
        return 500;
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
