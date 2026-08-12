package com.mailally.email.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Circuit breaker protecting provider from overload during repeated HTTP 5xx/429 errors or timeouts.
 * 
 * States: CLOSED -> OPEN -> HALF_OPEN -> CLOSED
 * 
 * Opens after FAILURE_THRESHOLD consecutive failures (5xx/timeout) or RATE_LIMIT_THRESHOLD
 * consecutive 429 responses. During OPEN state, all provider requests are paused for COOLDOWN_MS.
 * After cooldown, transitions to HALF_OPEN to test a single probe request.
 */
@Component
public class ProviderCircuitBreaker {

    private static final Logger log = LoggerFactory.getLogger(ProviderCircuitBreaker.class);

    public enum State { CLOSED, OPEN, HALF_OPEN }

    private static final int FAILURE_THRESHOLD = 5;        // consecutive 5xx/timeouts before OPEN
    private static final int RATE_LIMIT_THRESHOLD = 3;     // consecutive 429s before OPEN
    private static final long DEFAULT_COOLDOWN_MS = 30000; // 30s default cooldown

    private final ConcurrentHashMap<String, State> providerStateMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> failureCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> rateLimitCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> lastStateChangeMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> cooldownOverrideMap = new ConcurrentHashMap<>();

    public boolean allowRequest(String providerName) {
        String key = normalize(providerName);
        State currentState = providerStateMap.getOrDefault(key, State.CLOSED);

        if (currentState == State.CLOSED) {
            return true;
        }

        long now = System.currentTimeMillis();
        long lastChange = lastStateChangeMap.getOrDefault(key, now);
        long cooldown = cooldownOverrideMap.getOrDefault(key, DEFAULT_COOLDOWN_MS);

        if (currentState == State.OPEN) {
            if (now - lastChange > cooldown) {
                log.info("[CIRCUIT BREAKER]: Transitioning provider '{}' from OPEN to HALF_OPEN after {}ms cooldown.", key, cooldown);
                providerStateMap.put(key, State.HALF_OPEN);
                lastStateChangeMap.put(key, now);
                return true;
            }
            return false;
        }

        // HALF_OPEN: allow a single probe request
        return true;
    }

    public void recordSuccess(String providerName) {
        String key = normalize(providerName);
        failureCounts.put(key, new AtomicInteger(0));
        rateLimitCounts.put(key, new AtomicInteger(0));
        cooldownOverrideMap.remove(key);
        State currentState = providerStateMap.getOrDefault(key, State.CLOSED);

        if (currentState != State.CLOSED) {
            log.info("[CIRCUIT BREAKER]: Provider '{}' recovered. Resetting circuit to CLOSED.", key);
            providerStateMap.put(key, State.CLOSED);
            lastStateChangeMap.put(key, System.currentTimeMillis());
        }
    }

    /**
     * Record a non-429 failure (5xx, timeout, connection error).
     */
    public void recordFailure(String providerName) {
        String key = normalize(providerName);
        AtomicInteger count = failureCounts.computeIfAbsent(key, k -> new AtomicInteger(0));
        int total = count.incrementAndGet();

        if (total >= FAILURE_THRESHOLD) {
            openCircuit(key, DEFAULT_COOLDOWN_MS, "5xx/timeout failures", total);
        }
    }

    /**
     * Record a 429 rate-limit response. Optionally accepts provider's Retry-After seconds.
     */
    public void recordRateLimit(String providerName, int retryAfterSeconds) {
        String key = normalize(providerName);
        AtomicInteger count = rateLimitCounts.computeIfAbsent(key, k -> new AtomicInteger(0));
        int total = count.incrementAndGet();

        // Use provider's Retry-After if > 0, otherwise use escalating backoff
        long cooldownMs;
        if (retryAfterSeconds > 0) {
            cooldownMs = retryAfterSeconds * 1000L;
        } else {
            // Escalating backoff: 10s, 20s, 30s, 60s
            cooldownMs = Math.min(60000L, 10000L * total);
        }

        if (total >= RATE_LIMIT_THRESHOLD) {
            openCircuit(key, cooldownMs, "HTTP 429 rate limiting", total);
        } else {
            // Even below threshold, store the cooldown so workers can respect Retry-After
            cooldownOverrideMap.put(key, cooldownMs);
            log.warn("[CIRCUIT BREAKER]: Provider '{}' returned 429 (count={}/{}). Backoff {}ms.",
                    key, total, RATE_LIMIT_THRESHOLD, cooldownMs);
        }
    }

    private void openCircuit(String key, long cooldownMs, String reason, int count) {
        State currentState = providerStateMap.getOrDefault(key, State.CLOSED);
        if (currentState != State.OPEN) {
            log.warn("[CIRCUIT BREAKER]: Provider '{}' exceeded threshold ({} x {}). Opening circuit for {}ms.",
                    key, count, reason, cooldownMs);
            providerStateMap.put(key, State.OPEN);
            cooldownOverrideMap.put(key, cooldownMs);
            lastStateChangeMap.put(key, System.currentTimeMillis());
        }
    }

    public State getState(String providerName) {
        return providerStateMap.getOrDefault(normalize(providerName), State.CLOSED);
    }

    private String normalize(String providerName) {
        return providerName != null ? providerName.toUpperCase() : "DEFAULT";
    }
}
