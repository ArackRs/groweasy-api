package com.groweasy.groweasyapi.monitoring.controller;

import com.groweasy.groweasyapi.monitoring.model.dto.request.DeviceDataRequest;
import com.groweasy.groweasyapi.monitoring.model.dto.response.DeviceConfigResponse;
import com.groweasy.groweasyapi.monitoring.services.MonitoringService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/monitoring")
@Tag(name = "Monitoring Controller", description = "API for monitoring operations")
public class MonitoringController {

    private final MonitoringService monitoringService;

    @PostMapping("/{serialNumber}/data")
    public ResponseEntity<Void> receiveData(@PathVariable String serialNumber, @RequestBody DeviceDataRequest data) {

        monitoringService.receiveData(serialNumber, data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{serialNumber}/config")
    public ResponseEntity<DeviceConfigResponse> getConfig(@PathVariable String serialNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(monitoringService.getConfig(serialNumber));
    }
}
