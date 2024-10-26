package com.groweasy.groweasyapi.monitoring.controller;

import com.groweasy.groweasyapi.monitoring.model.dto.request.DeviceConfigRequest;
import com.groweasy.groweasyapi.monitoring.model.dto.request.DeviceDataRequest;
import com.groweasy.groweasyapi.monitoring.model.dto.response.DeviceConfigResponse;
import com.groweasy.groweasyapi.monitoring.model.dto.response.DeviceDataResponse;
import com.groweasy.groweasyapi.monitoring.model.dto.response.MetricResponse;
import com.groweasy.groweasyapi.monitoring.services.MonitoringService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/monitoring/devices")
@Tag(name = "Monitoring Controller", description = "API for monitoring operations")
public class MonitoringController {

    private final MonitoringService monitoringService;

    @PostMapping("/data")
    public ResponseEntity<Void> receiveData(@RequestBody DeviceDataRequest data) {

        monitoringService.receiveData(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/data")
    public ResponseEntity<DeviceDataResponse> getData() {
        return ResponseEntity.status(HttpStatus.OK).body(monitoringService.getData());
    }

    @GetMapping("/metrics")
    public ResponseEntity<List<MetricResponse>> getMetrics() {
        return ResponseEntity.status(HttpStatus.OK).body(monitoringService.getMetrics());
    }

    @GetMapping("/config")
    public ResponseEntity<DeviceConfigResponse> getConfig() {
        return ResponseEntity.status(HttpStatus.OK).body(monitoringService.getConfig());
    }

    @PutMapping("/config")
    public ResponseEntity<DeviceConfigResponse> updateConfig(@RequestBody DeviceConfigRequest config) {

        DeviceConfigResponse response = monitoringService.updateConfig(config);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
