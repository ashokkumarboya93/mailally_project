package com.mailally.email.queue;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Multi-tenant rate limiter using the Token Bucket algorithm backed by Redis.
 */
@Service
public class RedisRateLimiter {

    private final StringRedisTemplate redisTemplate;

    public RedisRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Attempts to acquire a token for the specified organization based on their subscription tier.
     *
     * @param organizationId the organization ID
     * @param tier           the subscription tier (ENTERPRISE, PROFESSIONAL, STARTER)
     * @return true if token is acquired (allowed), false if rate-limited
     */
    public boolean acquireToken(Long organizationId, String tier) {
        int limitPerSec = getTierRateLimit(tier);
        String tokensKey = "ratelimit:org:" + organizationId + ":tokens";
        String lastRefillKey = "ratelimit:org:" + organizationId + ":last_refill";

        long now = System.currentTimeMillis();

        String lastRefillStr = redisTemplate.opsForValue().get(lastRefillKey);
        String tokensStr = redisTemplate.opsForValue().get(tokensKey);

        double tokens;
        long lastRefill = now;

        if (tokensStr == null || lastRefillStr == null) {
            tokens = limitPerSec;
        } else {
            try {
                lastRefill = Long.parseLong(lastRefillStr);
                double elapsedSeconds = (double) (now - lastRefill) / 1000.0;
                double currentTokens = Double.parseDouble(tokensStr);

                // Add leaked tokens up to capacity (limitPerSec)
                tokens = Math.min(limitPerSec, currentTokens + (elapsedSeconds * limitPerSec));
            } catch (NumberFormatException e) {
                tokens = limitPerSec;
            }
        }

        if (tokens >= 1.0) {
            tokens -= 1.0;
            redisTemplate.opsForValue().set(tokensKey, String.valueOf(tokens));
            redisTemplate.opsForValue().set(lastRefillKey, String.valueOf(now));
            return true;
        }

        return false;
    }

    private int getTierRateLimit(String tier) {
        if (tier == null) return 5;
        switch (tier.toUpperCase()) {
            case "ENTERPRISE":
                return 100;
            case "PROFESSIONAL":
                return 20;
            case "STARTER":
            default:
                return 5;
        }
    }
}
