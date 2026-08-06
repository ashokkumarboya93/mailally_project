package com.mailally.email.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * High-speed caching service for campaign stats to minimize database write operations.
 */
@Service
public class RedisProgressCache {

    private static final Logger log = LoggerFactory.getLogger(RedisProgressCache.class);
    private static final String SENT_KEY = "campaign:%d:progress:sent";
    private static final String FAILED_KEY = "campaign:%d:progress:failed";

    private final StringRedisTemplate redisTemplate;

    public RedisProgressCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void incrementSent(Long campaignId) {
        redisTemplate.opsForValue().increment(String.format(SENT_KEY, campaignId));
    }

    public void incrementFailed(Long campaignId) {
        redisTemplate.opsForValue().increment(String.format(FAILED_KEY, campaignId));
    }

    public Map<String, Integer> getProgress(Long campaignId) {
        String sentStr = redisTemplate.opsForValue().get(String.format(SENT_KEY, campaignId));
        String failedStr = redisTemplate.opsForValue().get(String.format(FAILED_KEY, campaignId));

        int sent = sentStr != null ? Integer.parseInt(sentStr) : 0;
        int failed = failedStr != null ? Integer.parseInt(failedStr) : 0;

        Map<String, Integer> progress = new HashMap<>();
        progress.put("sent", sent);
        progress.put("failed", failed);
        return progress;
    }

    public void clear(Long campaignId) {
        redisTemplate.delete(String.format(SENT_KEY, campaignId));
        redisTemplate.delete(String.format(FAILED_KEY, campaignId));
        log.info("RedisProgressCache: Cleared keys for campaignId={}", campaignId);
    }
}
