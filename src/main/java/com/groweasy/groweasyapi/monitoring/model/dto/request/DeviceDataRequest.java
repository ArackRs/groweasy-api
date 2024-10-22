package com.groweasy.groweasyapi.monitoring.model.dto.request;

public record DeviceDataRequest(
        Double temperature,
        Double humidity,
        Double luminosity
) {
}
