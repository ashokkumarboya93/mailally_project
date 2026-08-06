package com.mailally.email.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Consumer processing email delivery events to update real-time analytical records.
 */
@Service
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class AnalyticsConsumer {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsConsumer.class);

    @KafkaListener(topics = "email.delivery", groupId = "mailally-analytics-group")
    public void consumeDeliveryEvent(Map<String, Object> event) {
        try {
            Long campaignId = Long.valueOf(event.get("campaignId").toString());
            String status = (String) event.get("status");

            log.info("AnalyticsConsumer: Received delivery event for campaignId={} status={}", campaignId, status);
            
            // Perform asynchronous aggregations and update cache parameters
            // (Phase 6 will link this directly to the Redis Progress Cache and WebSocket push)
        } catch (Exception e) {
            log.error("AnalyticsConsumer: Error processing event payload: {}", e.getMessage());
        }
    }
}
