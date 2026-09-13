package com.docMind.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

  @Bean(name = "documentTaskExecutor")
  public Executor documentTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    // Core number of threads
    executor.setCorePoolSize(2);

    // Maximum number of threads
    executor.setMaxPoolSize(4);

    // Queue capacity before rejecting tasks
    executor.setQueueCapacity(50);

    // Name prefix for easier debugging
    executor.setThreadNamePrefix("doc-processor-");

    executor.initialize();
    return executor;
  }
}