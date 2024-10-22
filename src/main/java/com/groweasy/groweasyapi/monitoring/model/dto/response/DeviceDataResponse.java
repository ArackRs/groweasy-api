package com.groweasy.groweasyapi.monitoring.model.dto.response;

import java.time.LocalDateTime;

public record DeviceDataResponse(
        Long id,
        Double temperature,
        Double humidity,
        Double luminosity,
        LocalDateTime timestamp) {
}