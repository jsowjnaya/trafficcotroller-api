package com.js.traffic.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.js.traffic.exception.JunctionNotFoundException;
import com.js.traffic.model.Junction;
import com.js.traffic.model.Lights;
import com.js.traffic.repository.JunctionRepository;
import com.js.traffic.repository.LightsRepository;

import jakarta.transaction.Transactional;

@Service

public class TrafficControllerService {
	private static final Logger logger = LoggerFactory.getLogger(TrafficControllerService.class);

	@Autowired
	JunctionRepository repository;

	@Autowired
	LightsRepository lightsRepository; //

	public Junction save(Junction junction) {
		if (junction == null) {
			throw new JunctionNotFoundException("Junction object cannot be null");
		}
		return repository.save(junction);
	}

	public List<Junction> getAllJunInfo() {
		List<Junction> junctions = repository.findAll();
		if (junctions.isEmpty()) {
			throw new JunctionNotFoundException("No junctions found");
		}
		return junctions;
	}

	public List<Lights> getAllLightsInfo(Integer jid) {
		if (jid == null) {
			throw new JunctionNotFoundException("Junction id cannot be null");
		}

		List<Lights> lights = lightsRepository.findByJid(jid);

		return lights;

	}

	@Transactional
	public Junction cmdPauseResume(Integer jid, String cmd) {
		Junction junction = repository.findById(jid)
				.orElseThrow(() -> new RuntimeException("Intersection id not found :" + jid));
		junction.setCmd(cmd);
		return junction;
	}

	@Transactional
	public List<Lights> sequenceChnage(Integer jid, String sequence) {
		List<Lights> lightsList = lightsRepository.findByJid(jid);

		String[] seqarr = sequence.split("-", 2);
		String directionCode = seqarr[0].toUpperCase();
		Integer seqDuration = Integer.parseInt(seqarr[1]);
		for (Lights lights : lightsList) {

			switch (directionCode) {
			case "NS":
				if (lights.getDirection().equalsIgnoreCase("NORTH")
						|| lights.getDirection().equalsIgnoreCase("SOUTH")) {
					lights.setDuration(seqDuration);
				}
				break;
			case "EW":
				if (lights.getDirection().equalsIgnoreCase("EAST") || lights.getDirection().equalsIgnoreCase("WEST")) {
					lights.setDuration(seqDuration);
				}
				break;
			}
		}
		return lightsList;
	}

}
