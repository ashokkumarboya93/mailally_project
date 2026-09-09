package com.mailally.email.service;

import com.mailally.email.repository.CampaignRecipientLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service providing multi-tier idempotency verification for campaign dispatches.
 * Prevents duplicate email sends under network retries, worker restarts, or duplicate events.
 */
@Service
public class EmailIdempotencyService {

    private static final Logger log = LoggerFactory.getLogger(EmailIdempotencyService.class);

    private final CampaignRecipientLogRepository recipientLogRepository;
    private final ConcurrentHashMap<String, Boolean> inMemoryDispatchedCache = new ConcurrentHashMap<>();

    public EmailIdempotencyService(CampaignRecipientLogRepository recipientLogRepository) {
        this.recipientLogRepository = recipientLogRepository;
    }

    /**
     * Generates a deterministic idempotency key for a specific campaign recipient.
     */
    public String generateIdempotencyKey(Long campaignId, String recipientEmail) {
        String raw = campaignId + ":" + (recipientEmail != null ? recipientEmail.toLowerCase().trim() : "");
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return "IDEM-" + raw.hashCode();
        }
    }

    /**
     * Checks if a campaign recipient has already been sent an email.
     */
    public boolean isAlreadyProcessed(Long campaignId, String recipientEmail) {
        String key = generateIdempotencyKey(campaignId, recipientEmail);

        // Fast path 1: Check in-memory dispatch cache
        if (inMemoryDispatchedCache.containsKey(key)) {
            return true;
        }

        // Fast path 2: Check database recipient log repository
        boolean existsInDb = recipientLogRepository.existsByCampaignIdAndEmail(campaignId, recipientEmail);
        if (existsInDb) {
            inMemoryDispatchedCache.put(key, true);
            return true;
        }

        return false;
    }

    /**
     * Marks a job key as processed in the runtime idempotency cache.
     */
    public void markProcessed(Long campaignId, String recipientEmail) {
        String key = generateIdempotencyKey(campaignId, recipientEmail);
        inMemoryDispatchedCache.put(key, true);
    }

    /**
     * Evicts cached idempotency keys for a campaign upon completion.
     */
    public void clearCampaignCache(Long campaignId) {
        inMemoryDispatchedCache.keySet().removeIf(k -> k.startsWith("IDEM-" + campaignId));
    }
}
