package com.groweasy.groweasyapi.monitoring.model.dto.response;

import com.groweasy.groweasyapi.monitoring.model.entities.DeviceConfig;

public record SensorConfigResponse(
        Long id,
        Double min,
        Double max,
        Double threshold
) {
    public static SensorConfigResponse fromEntity(DeviceConfig newConfig) {
        return new SensorConfigResponse(
                newConfig.getId(),
                newConfig.getHumMin(),
                newConfig.getTempMax(),
                newConfig.getHumThreshold()
        );
    }
}
