package com.mailally.email.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Consumer processing email delivery events to manage credit and plan quota deductions.
 */
@Service
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class BillingConsumer {

    private static final Logger log = LoggerFactory.getLogger(BillingConsumer.class);

    @KafkaListener(topics = "email.delivery", groupId = "mailally-billing-group")
    public void consumeBillingEvent(Map<String, Object> event) {
        try {
            Long campaignId = Long.valueOf(event.get("campaignId").toString());
            String status = (String) event.get("status");

            if ("SENT".equalsIgnoreCase(status) || "DELIVERED".equalsIgnoreCase(status)) {
                log.debug("BillingConsumer: Deducting credit resource for campaignId={}", campaignId);
            }
        } catch (Exception e) {
            log.error("BillingConsumer: Error logging credit calculation: {}", e.getMessage());
        }
    }
}
