package com.groweasy.groweasyapi.monitoring.controllers;

import com.groweasy.groweasyapi.monitoring.model.entities.SensorData;
import com.groweasy.groweasyapi.monitoring.services.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/monitoring")
public class MonitoringController {

    @Autowired
    private MonitoringService monitoringService;

    @GetMapping("/data")
    public List<SensorData> getAllSensorData() {
        return monitoringService.getAllSensorData();
    }

    @PostMapping("/data")
    public SensorData addSensorData(@RequestBody SensorData sensorData) {
        return monitoringService.saveSensorData(sensorData);
    }

    @GetMapping("/data/thresholds")
    public String checkThresholds() {
        return monitoringService.checkThresholds();
    }
}
