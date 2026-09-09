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
    /**
     * Thread pool for asynchronous email campaign orchestration.
     * - Core pool: 10 threads (handles 10 concurrent campaigns)
     * - Max pool: 25 threads
     * - Queue capacity: 1000
     */
    @Bean(name = "emailTaskExecutor")
    public Executor emailTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(25);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("MailAlly-Campaign-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }

    /**
     * Bulkhead worker thread pool specifically for sub-task parallel email recipient dispatches.
     * High-concurrency worker pool allowing 25-50 emails to be sent in parallel.
     */
    @Bean(name = "emailBatchWorkerExecutor")
    public Executor emailBatchWorkerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(25);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(2000);
        executor.setThreadNamePrefix("MailAlly-Worker-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }

    /**
     * Bulkhead worker thread pool for file processing (CSV/Excel ingestion).
     */
    @Bean(name = "fileProcessingExecutor")
    public Executor fileProcessingExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("MailAlly-FileProcessor-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }
}
