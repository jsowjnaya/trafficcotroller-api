package com.js.traffic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.js.traffic.model.Junction;
import com.js.traffic.model.Lights;
import com.js.traffic.repository.JunctionRepository;
import com.js.traffic.repository.LightsRepository;

class TrafficSchedulerServiceTest {

	@Mock
	private JunctionRepository junctionRepository;

	@Mock
	private LightsRepository lightsRepository;

	@InjectMocks
	private TrafficSchedulerService trafficSchedulerService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	//@Test
	void testTrafficScheduler_NSGreen() {
		trafficSchedulerService.schedulerStartTime = LocalDateTime.now().minusSeconds(1);
		// trafficSchedulerService.schedulerStartTime = LocalDateTime.of(2026, 2, 14,
		// 20, 56, 0);
		Junction junction = new Junction();
		junction.setJid(1);
		junction.setCmd("resume");

		// Setup lights for NS green
		Lights nsLight = new Lights();
		nsLight.setJid(1);
		nsLight.setCode("NS");
		nsLight.setColor("GREEN");
		nsLight.setDirection("NORTH");
		nsLight.setCstatus("NS-GREEN");

		Lights ewLight = new Lights();
		ewLight.setJid(1);
		ewLight.setCode("EW");
		ewLight.setDirection("EAST");
		ewLight.setColor("RED");
		ewLight.setCstatus("EW-RED");
		// Mock repository calls
        
		when(junctionRepository.findAll()).thenReturn(Collections.singletonList(junction));
		when(lightsRepository.findByColorAndJid("GREEN", 1)).thenReturn(Collections.singletonList(nsLight));
		when(lightsRepository.findByColorAndJid("YELLOW", 1)).thenReturn(Collections.emptyList());
		when(lightsRepository.findByJid(1)).thenReturn(Arrays.asList(nsLight, ewLight));
		// Calling scheduler directly
		trafficSchedulerService.TrafficScheduler();
		assertNotEquals(nsLight.getColor(), ewLight.getColor(), "NS and EW lights should not have the same color");
		assertNotEquals(nsLight.getCstatus(), ewLight.getCstatus(), "NS and EW cstatus should not conflict");
		
		verify(junctionRepository, times(1)).findAll();
		verify(lightsRepository, times(1)).findByColorAndJid("GREEN", 1);
		verify(lightsRepository, times(1)).findByColorAndJid("YELLOW", 1);
		verify(lightsRepository, times(1)).findByJid(1);
	}

	@Test
	void testTrafficScheduler_NoGreenLights() {
		Junction junction = new Junction();
		junction.setJid(2);
		junction.setCmd("resume");

		Lights yellowLight = new Lights();
		yellowLight.setJid(2);
		yellowLight.setCode("NS");
		yellowLight.setColor("YELLOW");
		yellowLight.setCstatus("NS-YELLOW");

		when(junctionRepository.findAll()).thenReturn(Collections.singletonList(junction));
		when(lightsRepository.findByColorAndJid("GREEN", 2)).thenReturn(Collections.emptyList());
		when(lightsRepository.findByColorAndJid("YELLOW", 2)).thenReturn(Collections.singletonList(yellowLight));
		when(lightsRepository.findByJid(2)).thenReturn(Collections.singletonList(yellowLight));

		trafficSchedulerService.TrafficScheduler();

		assert (yellowLight.getColor().equals("YELLOW") || yellowLight.getColor().equals("RED"));
		assert (yellowLight.getCstatus().startsWith("NS-"));

		verify(junctionRepository, times(1)).findAll();
	}
}
