package com.mailally.integration.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OAuthStateCache {

    public static class StateData {
        private final Long userId;
        private final Long organizationId;
        private final long expiresAt;

        public StateData(Long userId, Long organizationId, long ttlMillis) {
            this.userId = userId;
            this.organizationId = organizationId;
            this.expiresAt = System.currentTimeMillis() + ttlMillis;
        }

        public Long getUserId() {
            return userId;
        }

        public Long getOrganizationId() {
            return organizationId;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiresAt;
        }
    }

    private final Map<String, StateData> stateStore = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();
    private static final long STATE_TTL_MILLIS = 5 * 60 * 1000L; // 5 minutes

    public String generateState(Long userId, Long organizationId) {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String stateToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        // Store state data
        stateStore.put(stateToken, new StateData(userId, organizationId, STATE_TTL_MILLIS));
        cleanupExpiredStates();
        return stateToken;
    }

    public StateData consumeState(String stateToken) {
        if (stateToken == null || stateToken.isBlank()) {
            return null;
        }
        StateData data = stateStore.remove(stateToken); // Single-use consumption
        if (data == null || data.isExpired()) {
            return null;
        }
        return data;
    }

    private void cleanupExpiredStates() {
        stateStore.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
}
