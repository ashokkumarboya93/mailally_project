package com.mailally.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Configuration defining Micrometer custom metrics for MailAlly monitoring and alerting.
 */
@Configuration
public class ObservabilityConfig {

    private static final String PENDING_KEY = "campaign:queue:pending";
    private static final String RETRY_KEY = "campaign:queue:retry";
    private static final String DLQ_KEY = "campaign:queue:dlq";

    private final StringRedisTemplate redisTemplate;

    public ObservabilityConfig(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public MeterBinder redisQueueMetrics() {
        return registry -> {
            Gauge.builder("mailally.queue.pending.size", this, value -> getPendingQueueSize())
                    .description("Current number of pending batches in Redis stream queue")
                    .register(registry);

            Gauge.builder("mailally.queue.retry.size", this, value -> getRetryQueueSize())
                    .description("Current number of retrying dispatches in Redis sorted set")
                    .register(registry);

            Gauge.builder("mailally.queue.dlq.size", this, value -> getDLQSize())
                    .description("Current number of failed dispatches in Redis dead letter queue list")
                    .register(registry);
        };
    }

    private double getPendingQueueSize() {
        try {
            Long size = redisTemplate.opsForStream().size(PENDING_KEY);
            return size != null ? size.doubleValue() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private double getRetryQueueSize() {
        try {
            Long size = redisTemplate.opsForZSet().zCard(RETRY_KEY);
            return size != null ? size.doubleValue() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private double getDLQSize() {
        try {
            Long size = redisTemplate.opsForList().size(DLQ_KEY);
            return size != null ? size.doubleValue() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
}
