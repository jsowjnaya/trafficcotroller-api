package com.js.traffic.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.js.traffic.exception.JunctionNotFoundException;
import com.js.traffic.model.Junction;
import com.js.traffic.model.Lights;
import com.js.traffic.service.TrafficControllerService;


@RestController
public class TrafficLightController {
	private static final Logger logger = LoggerFactory.getLogger(TrafficLightController.class);

	@Autowired
	TrafficControllerService trafficControllerService;


	/**
	 * test api
	 * @return
	 */
	@GetMapping("/v1/api/test")
	public String getResource() {
		return "Test Passed";

	}

	/**
	 * Gives all the Intersections information 
	 * @return
	 */
	@GetMapping("/v1/api/juninfo")
	public List<Junction> getAllJunInfo() {
		return trafficControllerService.getAllJunInfo();
	}
	
	/**
	 * Gives the Traffic lights information by Junction Id 
	 * @param jid
	 * @return
	 */

	@GetMapping("/v1/api/lightinfo/{jid}") //
	public List<Lights> getAllLightsInfo(@PathVariable Integer jid) {
		if (jid == null) {
	        throw new JunctionNotFoundException("Junction ID cannot be null");
	    }
		return trafficControllerService.getAllLightsInfo(jid);
	}

	/**
	 * This api will allow you to pause or resume operation by Junction id 
	 * when it is paused ,PAUSE operation will stop auto processing from Traffic scheduler   
	 * when it is resumed ,RESUME operation will continue auto processing from Traffic scheduler 
	 * @param jid
	 * @param cmd
	 * @return
	 */
	@PostMapping("/v1/api/intersect/operation/{jid}/{cmd}")
	public Junction cmdPauseResume(@PathVariable Integer jid, @PathVariable String cmd) {
		logger.info("cmdPauseResume called");
		
		if (jid == null) {
			throw new JunctionNotFoundException("Junction ID cannot be null");
		}

		if (cmd == null || cmd.isBlank()) {
			throw new JunctionNotFoundException("Command cannot be null or blank");
		}
		 
		return trafficControllerService.cmdPauseResume(jid, cmd);
	}

	/**
	 * This api will allow you to update duration for traffic lights 
	 * @param jid
	 * @return duration chane to ection reduce/increase
	 */

	@PostMapping("/v1/api/change/seq/{jid}/{sequence}")
	public List<Lights> sequenceChnage(@PathVariable Integer jid, @PathVariable String sequence) {
		return trafficControllerService.sequenceChnage(jid, sequence);
	}

	
}
