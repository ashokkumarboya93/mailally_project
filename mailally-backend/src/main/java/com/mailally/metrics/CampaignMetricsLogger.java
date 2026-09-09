package com.mailally.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Enterprise metrics recorder providing Micrometer/Prometheus observability.
 * Exposes latency, throughput, and error metrics for production dashboards.
 */
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@Component
public class CampaignMetricsLogger {

    private final MeterRegistry meterRegistry;
    private final Counter sentCounter;
    private final Counter failedCounter;
    private final Timer dispatchTimer;

    @Autowired(required = false)
    public CampaignMetricsLogger(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry != null ? meterRegistry : new SimpleMeterRegistry();
        this.sentCounter = Counter.builder("mailally.emails.sent.total")
                .description("Total emails successfully sent")
                .register(this.meterRegistry);
        this.failedCounter = Counter.builder("mailally.emails.failed.total")
                .description("Total email dispatch failures")
                .register(this.meterRegistry);
        this.dispatchTimer = Timer.builder("mailally.campaign.dispatch.latency")
                .description("Latency of campaign email dispatches")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(this.meterRegistry);
    }

    public void recordSent() {
        sentCounter.increment();
    }

    public void recordFailed() {
        failedCounter.increment();
    }

    public void recordDispatchLatency(long durationMs) {
        dispatchTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }
}
