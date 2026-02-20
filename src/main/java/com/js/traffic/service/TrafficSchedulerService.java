package com.js.traffic.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.js.traffic.model.Junction;
import com.js.traffic.model.Lights;
import com.js.traffic.repository.JunctionRepository;
import com.js.traffic.repository.LightsRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TrafficSchedulerService {
	private static final Logger logger = LoggerFactory.getLogger(TrafficSchedulerService.class);

	@Autowired
	JunctionRepository repository;

	@Autowired
	LightsRepository lightsRepository;

	private String greenCode = "";
	private String yellowCode = "";

	private static final int greenDuration = 20;
	private static final int yellowDuration = 7;
	private static final int redDuration = 3;
	private static final int total_cycle = 30;
	public LocalDateTime schedulerStartTime;

	@Scheduled(cron = "*/30 * * * * *")

	public void TrafficScheduler() {

		logger.info("Trafic  sechedule is running.....30s.");
		if (schedulerStartTime == null) {
			schedulerStartTime = LocalDateTime.now();
			logger.info("Cycle Started At: {}", schedulerStartTime);
		}
		List<Junction> junctionList = repository.findAll();
		for (Junction junction : junctionList) {
			if (junction.getCmd().equalsIgnoreCase("resume")) {
				List<Lights> codes = lightsRepository.findByColorAndJid("GREEN", junction.getJid());
				if (!codes.isEmpty()) {
					greenCode = codes.get(0).getCode();
					logger.info("Active Green Code: " + greenCode);
				} else {
					List<Lights> ycodes = lightsRepository.findByColorAndJid("YELLOW", junction.getJid());
					if (!ycodes.isEmpty()) {
						yellowCode = ycodes.get(0).getCode();
						logger.info("Active Yellow Code: " + yellowCode);
					} else
						logger.info("No code found for junction");
				}
				List<Lights> lightsList = lightsRepository.findByJid(junction.getJid());
				long secondsPassed = Duration.between(schedulerStartTime, LocalDateTime.now()).getSeconds();

				long cycleSecond = secondsPassed % total_cycle;
				if (greenCode.equalsIgnoreCase("NS")) {
					// Step 1: NS GREEN → NS YELLOW
					if (cycleSecond < greenDuration + yellowDuration) {
						updateLights(lightsList, "NS", "YELLOW", secondsPassed);
						updateLights(lightsList, "EW", "RED", secondsPassed);
						logger.info("NS = YELLOW | EW = RED");
					} else {
						// After yellow duration, switch
						updateLights(lightsList, "NS", "RED", secondsPassed);
						updateLights(lightsList, "EW", "GREEN", secondsPassed);
						logger.info("NS = RED | EW = GREEN");
					}

				} else if (greenCode.equalsIgnoreCase("EW")) {
					// Step 1: EW GREEN → EW YELLOW
					if (cycleSecond < greenDuration + yellowDuration) {
						updateLights(lightsList, "EW", "YELLOW", secondsPassed);
						updateLights(lightsList, "NS", "RED", secondsPassed);
						logger.info("NS = RED | EW = YELLOW");
					} else {
						// After yellow duration, switch
						updateLights(lightsList, "EW", "RED", secondsPassed);
						updateLights(lightsList, "NS", "GREEN", secondsPassed);
						logger.info("NS = GREEN | EW = RED");
					}

				}

			}
		}

	}

	@Transactional
	private void updateLights(List<Lights> lightsList, String direction, String color, long secondsPassed) {

		lightsList.stream().filter(light -> light.getCode().equalsIgnoreCase(direction)).forEach(light -> {
			light.setColor(color);
			String dynamicStatus = direction + "-" + color;
			light.setCstatus(dynamicStatus);
		});

	}

}
