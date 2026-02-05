package com.viago.service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuration for asynchronous email processing.
 * Creates a dedicated thread pool for email sending operations.
 */
@Configuration
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    /**
     * Configure custom thread pool for async email operations.
     * 
     * @return Executor for async tasks
     */
    @Bean(name = "emailTaskExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Core pool size - minimum threads always alive
        executor.setCorePoolSize(5);

        // Max pool size - maximum concurrent threads
        executor.setMaxPoolSize(10);

        // Queue capacity - pending tasks before rejection
        executor.setQueueCapacity(100);

        // Thread name prefix for easy identification in logs
        executor.setThreadNamePrefix("email-async-");

        // Wait for tasks to complete on shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);

        // Maximum time to wait for shutdown (30 seconds)
        executor.setAwaitTerminationSeconds(30);

        executor.initialize();

        log.info("Email task executor initialized: corePoolSize={}, maxPoolSize={}, queueCapacity={}",
                executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getQueueCapacity());

        return executor;
    }
}
