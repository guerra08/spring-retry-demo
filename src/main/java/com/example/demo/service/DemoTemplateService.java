package com.example.demo.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.config.AppConfig;

@Service
public class DemoTemplateService {

    private final RetryTemplate retryTemplate;
    private final AppConfig appConfig;

    private static final Logger logger = LoggerFactory.getLogger(DemoTemplateService.class);


    public DemoTemplateService(AppConfig appConfig) {
        this.appConfig = appConfig;
        this.retryTemplate = RetryTemplate.builder()
                .maxAttempts(5)
                .fixedBackoff(1000)
                .retryOn(RuntimeException.class)
                .build();
    }

    public int callRemote() {
        return retryTemplate.execute(ctx -> {
            var random = new Random();
            var chance = random.nextInt(10);
            if (chance < 8) {
                logger.error("callRemote :: Remote call failed to {}", appConfig.externalServiceName());
                throw new RuntimeException("Remote call failed");
            }
            return chance;
        });
    }
}
