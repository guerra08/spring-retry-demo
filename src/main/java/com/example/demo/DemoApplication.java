package com.example.demo;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.demo.service.DemoService;
import com.example.demo.service.DemoTemplateService;

@SpringBootApplication
@EnableRetry
@EnableScheduling
@ConfigurationPropertiesScan
public class DemoApplication {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(DemoApplication.class);

	private DemoService demoService;
	private DemoTemplateService demoTemplateService;

	public DemoApplication(DemoService demoService, DemoTemplateService demoTemplateService) {
		this.demoService = demoService;
		this.demoTemplateService = demoTemplateService;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Scheduled(fixedDelayString = "${app.thread-sleep-seconds}")
	public void runDemoService() {
		var result = demoService.remoteCall();
		result.forEach((var value) -> log.info("Remote call result: {}", value));
	}

	@Scheduled(fixedDelayString = "${app.thread-sleep-seconds}")
	public void runDemoTemplateService() {
		var result = demoTemplateService.callRemote();
		log.info("Remote call with template result: {}", result);
	}

}
