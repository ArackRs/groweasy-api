package com.groweasy.groweasyapi.monitoring.controller;

import com.groweasy.groweasyapi.monitoring.model.dto.request.DeviceConfigRequest;
import com.groweasy.groweasyapi.monitoring.model.dto.response.DeviceConfigResponse;
import com.groweasy.groweasyapi.monitoring.model.dto.response.DeviceDataResponse;
import com.groweasy.groweasyapi.monitoring.model.dto.response.MetricResponse;
import com.groweasy.groweasyapi.monitoring.services.DeviceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/devices")
@Tag(name = "Device Controller", description = "API for device operations")
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/{deviceName}")
    public ResponseEntity<Void> registerDevice(@PathVariable String deviceName) {
        deviceService.registerDevice(deviceName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{deviceName}/config")
    public ResponseEntity<DeviceConfigResponse> updateConfig(@PathVariable String deviceName, @RequestBody DeviceConfigRequest config) {

        DeviceConfigResponse response = deviceService.updateConfig(deviceName, config);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{deviceName}")
    public ResponseEntity<DeviceDataResponse> getData(@PathVariable String deviceName) {
        return ResponseEntity.status(HttpStatus.OK).body(deviceService.getData(deviceName));
    }

    @GetMapping("/{deviceName}/metrics")
    public ResponseEntity<List<MetricResponse>> getMetrics(@PathVariable String deviceName) {
        return ResponseEntity.status(HttpStatus.OK).body(deviceService.getMetrics(deviceName));
    }
}
