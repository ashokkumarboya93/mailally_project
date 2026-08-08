package com.mailally.email.service;

import java.util.Map;

/**
 * Service interface for decoupled Webhook Resolution Engine and Dead-Letter Queue processing.
 */
public interface WebhookResolverService {
    /**
     * Resolves incoming webhook payload into a persisted EmailEvent with guaranteed organization_id and campaign_id.
     * If unresolvable, persists payload into UnresolvedWebhookEvent dead-letter queue.
     */
    boolean processWebhookEvent(Map<String, Object> payload, String provider);

    /**
     * Retries resolution of pending UnresolvedWebhookEvent dead-letter records.
     * @return count of successfully resolved events
     */
    int retryUnresolvedEvents();
}
