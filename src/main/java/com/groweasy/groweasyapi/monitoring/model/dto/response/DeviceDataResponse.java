package com.groweasy.groweasyapi.monitoring.model.dto.response;

import com.groweasy.groweasyapi.monitoring.model.entities.DeviceData;

import java.util.List;

public record DeviceDataResponse(
        Long id,
        String serialNumber,
        String status,
        String location,
        List<SensorResponse> sensors
) {
    public static DeviceDataResponse fromEntity(DeviceData deviceData) {
        return new DeviceDataResponse(
                deviceData.getId(),
                deviceData.getSerialNumber(),
                deviceData.getStatus().name(),
                deviceData.getLocation(),
                SensorResponse.fromEntityList(deviceData.getSensors())
        );
    }

    public static List<DeviceDataResponse> fromEntityList(List<DeviceData> allDevices) {
        return allDevices.stream()
                .map(DeviceDataResponse::fromEntity)
                .toList();
    }
}