package com.mailally.queue.outbox.service;

import com.mailally.queue.outbox.entity.OutboxEvent;
import com.mailally.queue.outbox.repository.OutboxEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Publisher service streaming transactional outbox events to Kafka topics.
 * Decouples core business transactions from external broker messaging.
 */
@Service
public class OutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.enabled:false}")
    private boolean kafkaEnabled;

    @Autowired
    public OutboxPublisher(OutboxEventRepository outboxEventRepository,
                           @Autowired(required = false) KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxEventRepository = outboxEventRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Polls pending outbox events every 2 seconds and publishes to Kafka.
     */
    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void processOutboxEvents() {
        List<OutboxEvent> pendingEvents = outboxEventRepository.findPendingEvents(PageRequest.of(0, 100));
        if (pendingEvents.isEmpty()) {
            return;
        }

        for (OutboxEvent event : pendingEvents) {
            try {
                if (kafkaEnabled && kafkaTemplate != null) {
                    String topic = "mailally." + event.getAggregateType().toLowerCase() + "." + event.getEventType().toLowerCase();
                    kafkaTemplate.send(topic, event.getAggregateId(), event.getPayload());
                    log.info("Outbox published event {} to Kafka topic {}", event.getId(), topic);
                }

                event.setStatus("PROCESSED");
                event.setProcessedAt(LocalDateTime.now());
                outboxEventRepository.save(event);
            } catch (Exception ex) {
                log.error("Failed to publish outbox event {}: {}", event.getId(), ex.getMessage(), ex);
                event.setRetryCount(event.getRetryCount() + 1);
                event.setErrorMessage(ex.getMessage());
                if (event.getRetryCount() >= 5) {
                    event.setStatus("FAILED");
                }
                outboxEventRepository.save(event);
            }
        }
    }
}
