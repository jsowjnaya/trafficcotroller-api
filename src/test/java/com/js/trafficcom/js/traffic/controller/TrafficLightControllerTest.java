package com.js.trafficcom.js.traffic.controller;

import com.js.traffic.controller.TrafficLightController;
import com.js.traffic.model.Junction;
import com.js.traffic.service.TrafficControllerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TrafficLightControllerTest {

    @Mock
    private TrafficControllerService trafficControllerService;

    @InjectMocks
    private TrafficLightController trafficLightController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllJunInfo() {
        Junction j1 = new Junction();
        j1.setJid(1);
        j1.setCmd("pause");
        j1.setCreatedAt(LocalDateTime.now());
        j1.setTitle("JUN01");
        
        Junction j2 = new Junction();
        j2.setJid(2);
        j2.setCmd("pause");
        j2.setCreatedAt(LocalDateTime.now());
        j2.setTitle("JUN02");

        when(trafficControllerService.getAllJunInfo()).thenReturn(Arrays.asList(j1,j2));

        List<Junction> result = trafficLightController.getAllJunInfo();

        assertEquals(2, result.size());
        assertEquals("JUN01", result.get(0).getTitle());
    }
}
