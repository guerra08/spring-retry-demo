package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.example.demo.config.AppConfig;

import java.util.Random;
import java.util.Set;

@Service
public class DemoService {

    private final AppConfig appConfig;

    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);

    public DemoService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 333))
    public Set<String> remoteCall() {
        Random r = new Random();
        int chance = r.nextInt(10);
        if (chance >= 5) {
            logger.info("remoteCall :: remote call success for {}", appConfig.externalServiceName());
            return Set.of("Hello, world!");
        } else {
            logger.error("remoteCall :: remote call failed for {}", appConfig.externalServiceName());
            throw new RuntimeException();
        }
    }

    @Recover
    public Set<String> remoteCallRecovery(RuntimeException e) {
        logger.error("remoteCall :: recovering");
        return Set.of("Failure, returning default message.");
    }

}
