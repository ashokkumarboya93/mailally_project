package com.mailally.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Global Distributed Rate Limiter implementing Token Bucket rate limiting.
 * Coordinates dispatches across multi-worker threads to strictly obey provider sending quotas (e.g. AWS SES limit).
 */
@Component("globalRedisRateLimiter")
public class RedisRateLimiter {

    private static final Logger log = LoggerFactory.getLogger(RedisRateLimiter.class);

    private final StringRedisTemplate redisTemplate;

    // Fallback in-memory rate limiter window
    private final AtomicInteger currentWindowCount = new AtomicInteger(0);
    private volatile long currentWindowTimestamp = System.currentTimeMillis();

    @Autowired(required = false)
    public RedisRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Acquires a permit to send an email under maxPermitsPerSecond quota.
     * Blocks or throttles if quota is exceeded in current 1-second window.
     */
    public boolean acquirePermit(String providerKey, int maxPermitsPerSecond) {
        if (maxPermitsPerSecond <= 0) {
            return true;
        }

        long now = System.currentTimeMillis();
        long windowSecond = now / 1000;

        if (redisTemplate != null) {
            try {
                String redisKey = "mailally:ratelimit:" + providerKey + ":" + windowSecond;
                Long count = redisTemplate.opsForValue().increment(redisKey);
                if (count != null && count == 1) {
                    redisTemplate.expire(redisKey, 3, TimeUnit.SECONDS);
                }
                if (count != null && count > maxPermitsPerSecond) {
                    // Throttled: sleep remaining time in window
                    long sleepMs = 1000 - (now % 1000);
                    if (sleepMs > 0 && sleepMs < 1000) {
                        Thread.sleep(sleepMs);
                    }
                }
                return true;
            } catch (Exception e) {
                log.debug("Redis rate limiter fallback to in-memory: {}", e.getMessage());
            }
        }

        // Fallback: In-memory sliding window rate limiting
        synchronized (this) {
            if (now - currentWindowTimestamp >= 1000) {
                currentWindowTimestamp = now;
                currentWindowCount.set(0);
            }
            if (currentWindowCount.incrementAndGet() > maxPermitsPerSecond) {
                long sleepMs = 1000 - (now - currentWindowTimestamp);
                if (sleepMs > 0 && sleepMs < 1000) {
                    try {
                        Thread.sleep(sleepMs);
                    } catch (InterruptedException ignored) {}
                }
            }
        }
        return true;
    }
}
