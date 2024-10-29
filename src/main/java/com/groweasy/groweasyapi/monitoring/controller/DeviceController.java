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

    @PostMapping
    public ResponseEntity<Void> registerDevice(@RequestParam Long id) {
        deviceService.connectDevice(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/config")
    public ResponseEntity<DeviceConfigResponse> updateConfig(@RequestBody DeviceConfigRequest config) {

        DeviceConfigResponse response = deviceService.updateConfig(config);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDataResponse> getDevice(@PathVariable Long id) {

        DeviceDataResponse response = DeviceDataResponse.fromEntity(deviceService.getDeviceById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{macAddress}/metrics")
    public ResponseEntity<List<MetricResponse>> getMetrics(@PathVariable String macAddress) {
        return ResponseEntity.status(HttpStatus.OK).body(deviceService.getMetrics(macAddress));
    }

    @GetMapping
    public ResponseEntity<List<DeviceDataResponse>> getAllDevices() {

        List<DeviceDataResponse> devices = DeviceDataResponse.fromEntityList(deviceService.getAllDevices());
        return ResponseEntity.status(HttpStatus.OK).body(devices);
    }
}
