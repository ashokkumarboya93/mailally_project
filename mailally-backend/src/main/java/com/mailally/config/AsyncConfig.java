package com.mailally.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Async configuration for background email campaign execution.
 * Provides a dedicated thread pool for campaign dispatching so the
 * HTTP request thread returns immediately to the client.
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Thread pool for asynchronous email campaign execution.
     * - Core pool: 5 threads (handles 5 concurrent campaigns)
     * - Max pool: 20 threads (burst capacity)
     * - Queue capacity: 500 (buffered campaign launches waiting for a thread)
     */
    @Bean(name = "emailTaskExecutor")
    public Executor emailTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("MailAlly-Campaign-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }
}
