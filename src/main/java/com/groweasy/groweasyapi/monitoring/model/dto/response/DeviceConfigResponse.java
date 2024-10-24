package com.groweasy.groweasyapi.monitoring.model.dto.response;

import com.groweasy.groweasyapi.monitoring.model.entities.DeviceConfig;

public record DeviceConfigResponse(
        Long id,
        int sampleInterval,
        double tempMin,
        double tempMax,
        double tempThreshold,
        double humMin,
        double humMax,
        double humThreshold,
        int lumMin,
        int lumMax,
        int lumThreshold
) {
    public static DeviceConfigResponse fromEntity(DeviceConfig entity) {
        return new DeviceConfigResponse(
                entity.getId(),
                entity.getSampleInterval(),
                entity.getTempMin(),
                entity.getTempMax(),
                entity.getTempThreshold(),
                entity.getHumMin(),
                entity.getHumMax(),
                entity.getHumThreshold(),
                entity.getLumMin(),
                entity.getLumMax(),
                entity.getLumThreshold()
        );
    }
}
