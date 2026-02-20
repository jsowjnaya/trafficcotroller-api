package com.js.traffic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TrafficcotrollerApiApplication {
	private static final Logger logger = LoggerFactory.getLogger(TrafficcotrollerApiApplication.class);

	public static void main(String[] args) {
		logger.info("Trafic Lights Controller API application started");
		SpringApplication.run(TrafficcotrollerApiApplication.class, args);
	}

}
