package com.mailally.email.scheduler;

import com.mailally.email.service.WebhookResolverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled background job that retries resolution for dead-letter UnresolvedWebhookEvent records every 30 seconds.
 */
@Component
public class UnresolvedWebhookRetryScheduler {

    private static final Logger log = LoggerFactory.getLogger(UnresolvedWebhookRetryScheduler.class);

    private final WebhookResolverService webhookResolverService;

    public UnresolvedWebhookRetryScheduler(WebhookResolverService webhookResolverService) {
        this.webhookResolverService = webhookResolverService;
    }

    @Scheduled(fixedDelay = 30000) // Run every 30 seconds
    public void retryUnresolvedWebhooks() {
        try {
            int resolved = webhookResolverService.retryUnresolvedEvents();
            if (resolved > 0) {
                log.info("[UNRESOLVED WEBHOOK SCHEDULER]: Resolved {} dead-letter webhook events.", resolved);
            }
        } catch (Exception e) {
            log.error("[UNRESOLVED WEBHOOK SCHEDULER]: Error executing retry job: {}", e.getMessage(), e);
        }
    }
}
