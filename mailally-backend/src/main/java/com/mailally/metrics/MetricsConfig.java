package com.mailally.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration defining a fallback {@link MeterRegistry} (SimpleMeterRegistry)
 * when Actuator or Prometheus metrics registry is not explicitly bound.
 */
@Configuration
public class MetricsConfig {

    @Bean
    @ConditionalOnMissingBean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }
}
