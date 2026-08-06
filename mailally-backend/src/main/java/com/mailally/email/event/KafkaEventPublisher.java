package com.mailally.email.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service responsible for publishing normalized delivery and state events to Kafka.
 */
@Service
public class KafkaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventPublisher(@Autowired(required = false) KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCampaignStateChange(Long campaignId, String status) {
        if (kafkaTemplate == null) {
            log.debug("Kafka is disabled or offline. Skipping publishCampaignStateChange for campaignId={}", campaignId);
            return;
        }
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("campaignId", campaignId);
            event.put("status", status);
            event.put("timestamp", System.currentTimeMillis());

            kafkaTemplate.send("campaign.lifecycle", String.valueOf(campaignId), event);
            log.debug("KafkaEventPublisher: Dispatched campaign.lifecycle event for campaignId={}", campaignId);
        } catch (Exception e) {
            log.error("KafkaEventPublisher: Failed to send campaign state event: {}", e.getMessage());
        }
    }

    public void publishEmailDeliveryEvent(Long campaignId, Long recipientId, String email, String status, String provider, String error) {
        if (kafkaTemplate == null) {
            log.debug("Kafka is disabled or offline. Skipping publishEmailDeliveryEvent for email={}", email);
            return;
        }
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("campaignId", campaignId);
            event.put("recipientId", recipientId);
            event.put("email", email);
            event.put("status", status);
            event.put("provider", provider);
            event.put("error", error);
            event.put("timestamp", System.currentTimeMillis());

            kafkaTemplate.send("email.delivery", String.valueOf(recipientId), event);
            log.debug("KafkaEventPublisher: Dispatched email.delivery event for recipientId={}", recipientId);
        } catch (Exception e) {
            log.error("KafkaEventPublisher: Failed to send email delivery event: {}", e.getMessage());
        }
    }
}
