package com.mailally.email.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Consumer processing lifecycle events to append audit logging trails for compliance.
 */
@Service
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class AuditConsumer {

    private static final Logger log = LoggerFactory.getLogger(AuditConsumer.class);

    @KafkaListener(topics = "campaign.lifecycle", groupId = "mailally-audit-group")
    public void consumeLifecycleEvent(Map<String, Object> event) {
        try {
            Long campaignId = Long.valueOf(event.get("campaignId").toString());
            String status = (String) event.get("status");

            log.info("AuditConsumer: Campaign [{}] transitioned to status={}", campaignId, status);
        } catch (Exception e) {
            log.error("AuditConsumer: Error logging lifecycle event: {}", e.getMessage());
        }
    }
}
