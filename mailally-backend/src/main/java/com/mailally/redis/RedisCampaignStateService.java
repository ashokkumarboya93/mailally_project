package com.mailally.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Service managing hot live campaign state, progress metrics, and telemetry in Redis.
 * Guarantees sub-millisecond atomic state access for live SSE dashboard polling.
 */
@Service
public class RedisCampaignStateService {

    private static final Logger log = LoggerFactory.getLogger(RedisCampaignStateService.class);
    private static final String CAMPAIGN_KEY_PREFIX = "mailally:campaign:";

    private final StringRedisTemplate redisTemplate;

    @Autowired(required = false)
    public RedisCampaignStateService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Initializes hot campaign progress telemetry counters in Redis.
     */
    public void initCampaignState(Long campaignId, int totalRecipients) {
        if (redisTemplate == null) return;
        try {
            String key = CAMPAIGN_KEY_PREFIX + campaignId + ":progress";
            Map<String, String> hash = new HashMap<>();
            hash.put("total", String.valueOf(totalRecipients));
            hash.put("sent", "0");
            hash.put("failed", "0");
            hash.put("status", "RUNNING");
            hash.put("updatedAt", String.valueOf(System.currentTimeMillis()));

            redisTemplate.opsForHash().putAll(key, hash);
            redisTemplate.expire(key, Duration.ofHours(24));
        } catch (Exception e) {
            log.warn("Redis unavailable for campaign {} init state: {}", campaignId, e.getMessage());
        }
    }

    /**
     * Atomically increments sent count in Redis.
     */
    public void incrementSent(Long campaignId) {
        if (redisTemplate == null) return;
        try {
            String key = CAMPAIGN_KEY_PREFIX + campaignId + ":progress";
            redisTemplate.opsForHash().increment(key, "sent", 1);
        } catch (Exception e) {
            log.debug("Redis INCRBY sent failed: {}", e.getMessage());
        }
    }

    /**
     * Atomically increments failed count in Redis.
     */
    public void incrementFailed(Long campaignId) {
        if (redisTemplate == null) return;
        try {
            String key = CAMPAIGN_KEY_PREFIX + campaignId + ":progress";
            redisTemplate.opsForHash().increment(key, "failed", 1);
        } catch (Exception e) {
            log.debug("Redis INCRBY failed: {}", e.getMessage());
        }
    }

    /**
     * Updates campaign status in Redis.
     */
    public void updateStatus(Long campaignId, String status) {
        if (redisTemplate == null) return;
        try {
            String key = CAMPAIGN_KEY_PREFIX + campaignId + ":progress";
            redisTemplate.opsForHash().put(key, "status", status);
        } catch (Exception e) {
            log.debug("Redis status update failed: {}", e.getMessage());
        }
    }
}
