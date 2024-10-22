package com.groweasy.groweasyapi.monitoring.controller;

import com.groweasy.groweasyapi.monitoring.model.dto.request.DeviceConfigRequest;
import com.groweasy.groweasyapi.monitoring.model.dto.request.DeviceDataRequest;
import com.groweasy.groweasyapi.monitoring.model.dto.response.DeviceConfigResponse;
import com.groweasy.groweasyapi.monitoring.services.DeviceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/devices")
@Tag(name = "Device Controller", description = "API for device operations")
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/data")
    public ResponseEntity<Void> receiveData(@RequestBody DeviceDataRequest data) {

        deviceService.receiveData(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/config")
    public ResponseEntity<DeviceConfigResponse> getConfig() {
        return ResponseEntity.status(HttpStatus.OK).body(deviceService.getConfig());
    }

    @PutMapping("/config")
    public ResponseEntity<DeviceConfigResponse> updateConfig(@RequestBody DeviceConfigRequest config) {

        DeviceConfigResponse response = deviceService.updateConfig(config);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
