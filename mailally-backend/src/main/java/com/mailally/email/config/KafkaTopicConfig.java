package com.mailally.email.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Configuration defining the Kafka topics registry for the MailAlly platform.
 */
@Configuration
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaTopicConfig {

    @Bean
    public NewTopic campaignLifecycleTopic() {
        return TopicBuilder.name("campaign.lifecycle")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic emailDeliveryTopic() {
        return TopicBuilder.name("email.delivery")
                .partitions(6)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic emailSuppressionTopic() {
        return TopicBuilder.name("email.suppression")
                .partitions(2)
                .replicas(1)
                .build();
    }
}
