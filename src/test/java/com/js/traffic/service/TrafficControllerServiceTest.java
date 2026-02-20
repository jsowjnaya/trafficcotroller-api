package com.js.traffic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.js.traffic.model.Lights;
import com.js.traffic.repository.LightsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

class TrafficControllerServiceTest {

    @Mock
    private LightsRepository lightsRepository;

    @InjectMocks
    private TrafficControllerService trafficControllerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLightsInfo() {
        Integer jid = 1;

        Lights light1 = new Lights();
        light1.setJid(jid);
        light1.setCode("NS");
        light1.setCstatus("NS-GREEN");
        light1.setDirection("NORTH");
        light1.setColor("GREEN");

        Lights light2 = new Lights();
        light2.setJid(jid);
        light2.setCode("NS");
        light2.setCstatus("NS-GREEN");
        light2.setDirection("SOUTH");
        light2.setColor("GREEN");
        
        Lights light3 = new Lights();
        light3.setJid(jid);
        light3.setCode("EW");
        light3.setCstatus("EW-RED");
        light3.setDirection("EAST");
        light3.setColor("RED");
        
        Lights light4 = new Lights();
        light4.setJid(jid);
        light4.setCode("EW");
        light4.setCstatus("EW-RED");
        light4.setDirection("WEST");
        light4.setColor("RED");

        List<Lights> mockLights = Arrays.asList(light1, light2,light3,light4);

        when(lightsRepository.findByJid(jid)).thenReturn(mockLights);

        List<Lights> result = trafficControllerService.getAllLightsInfo(jid);

        assertEquals(4, result.size());
        assertEquals("NS", result.get(0).getCode());
        verify(lightsRepository, times(1)).findByJid(jid);
    }
}

