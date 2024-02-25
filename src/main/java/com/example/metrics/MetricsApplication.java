package com.example.metrics;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@RestController
@Timed
@EnableScheduling
@SpringBootApplication
public class MetricsApplication {

	public static final String APPLICATION_COUNTER = "application.counter";

	private final MeterRegistry meterRegistry;
	private Counter counter = null;

	public static void main(String[] args) {
		SpringApplication.run(MetricsApplication.class, args);
	}

	@PostConstruct
	public void init() {
		counter = meterRegistry.counter(APPLICATION_COUNTER);
	}

	@Scheduled(initialDelay = 5L, fixedRate = 3L, timeUnit = TimeUnit.SECONDS)
	public void incrementCounter() {
		assert counter != null;
		counter.increment();
	}

	@GetMapping("/counter")
	@Timed("counter.api")
	public Double counter() {
		return counter.count();
	}
}
