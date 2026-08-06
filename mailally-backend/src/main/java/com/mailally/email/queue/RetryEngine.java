package com.mailally.email.queue;

import com.mailally.email.entity.CampaignRecipientLog;
import com.mailally.email.repository.CampaignRecipientLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service managing dispatches retries with exponential backoffs and Dead Letter Queue routing.
 */
@Service
@Transactional
public class RetryEngine {

    private static final Logger log = LoggerFactory.getLogger(RetryEngine.class);
    private static final String RETRY_KEY = "campaign:queue:retry";
    private static final String DLQ_KEY = "campaign:queue:dlq";

    private final StringRedisTemplate redisTemplate;
    private final CampaignRecipientLogRepository recipientRepository;

    public RetryEngine(StringRedisTemplate redisTemplate,
                       CampaignRecipientLogRepository recipientRepository) {
        this.redisTemplate = redisTemplate;
        this.recipientRepository = recipientRepository;
    }

    public void handleFailure(CampaignRecipientLog recipient, String errorMessage) {
        if (recipient == null) return;

        boolean isPermanent = isPermanentError(errorMessage);
        int maxRetries = 3;

        recipient.setLastError(errorMessage);

        if (isPermanent || recipient.getAttempts() >= maxRetries) {
            // Route to DLQ
            recipient.setStatus("FAILED");
            recipientRepository.save(recipient);
            
            pushToDLQ(recipient);
            log.info("RetryEngine: Permanent failure for recipient email={}. Routed to DLQ.", recipient.getEmail());
        } else {
            // Compute exponential backoff delay (attempts: 1 -> 5m, 2 -> 15m, 3 -> 1h)
            long delaySeconds = getBackoffDelaySeconds(recipient.getAttempts());
            long triggerTime = System.currentTimeMillis() + (delaySeconds * 1000);

            recipient.setStatus("RETRYING");
            recipientRepository.save(recipient);

            pushToRetryQueue(recipient, triggerTime);
            log.info("RetryEngine: Temporary failure for recipient email={} (attempt {}). Re-queued with backoff of {}s.",
                    recipient.getEmail(), recipient.getAttempts(), delaySeconds);
        }
    }

    private boolean isPermanentError(String error) {
        if (error == null) return false;
        String lower = error.toLowerCase();
        return lower.contains("invalid") 
            || lower.contains("suppressed") 
            || lower.contains("address rejected") 
            || lower.contains("550") 
            || lower.contains("bounce");
    }

    private long getBackoffDelaySeconds(int attempts) {
        if (attempts <= 1) return 300; // 5 minutes
        if (attempts == 2) return 900; // 15 minutes
        return 3600; // 1 hour
    }

    private void pushToRetryQueue(CampaignRecipientLog recipient, long triggerTime) {
        try {
            String payload = String.valueOf(recipient.getId());
            redisTemplate.opsForZSet().add(RETRY_KEY, payload, triggerTime);
        } catch (Exception e) {
            log.error("RetryEngine: Failed to push to retry queue: {}", e.getMessage());
        }
    }

    private void pushToDLQ(CampaignRecipientLog recipient) {
        try {
            String payload = String.format("{\"recipientId\":%d,\"email\":\"%s\",\"error\":\"%s\"}",
                    recipient.getId(), recipient.getEmail(), recipient.getLastError().replace("\"", "\\\""));
            redisTemplate.opsForList().rightPush(DLQ_KEY, payload);
        } catch (Exception e) {
            log.error("RetryEngine: Failed to push to DLQ: {}", e.getMessage());
        }
    }
}
