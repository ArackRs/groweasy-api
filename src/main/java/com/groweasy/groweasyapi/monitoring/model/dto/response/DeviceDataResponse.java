package com.groweasy.groweasyapi.monitoring.model.dto.response;

import com.groweasy.groweasyapi.monitoring.model.entities.DeviceData;

import java.util.List;

public record DeviceDataResponse(
        Long id,
        String status,
        String location,
        List<SensorResponse> sensors
) {
    public static DeviceDataResponse fromEntity(DeviceData deviceData) {
        return new DeviceDataResponse(
                deviceData.getId(),
                deviceData.getStatus().name(),
                deviceData.getLocation(),
                SensorResponse.fromEntityList(deviceData.getSensors())
        );
    }
}