package com.groweasy.groweasyapi.monitoring.services;

import com.groweasy.groweasyapi.loginregister.model.entities.UserEntity;
import com.groweasy.groweasyapi.loginregister.services.AuthService;
import com.groweasy.groweasyapi.monitoring.model.dto.request.DeviceConfigRequest;
import com.groweasy.groweasyapi.monitoring.model.dto.request.DeviceDataRequest;
import com.groweasy.groweasyapi.monitoring.model.dto.response.DeviceConfigResponse;
import com.groweasy.groweasyapi.monitoring.model.entities.DeviceConfig;
import com.groweasy.groweasyapi.monitoring.model.entities.Sensor;
import com.groweasy.groweasyapi.monitoring.model.entities.Metric;
import com.groweasy.groweasyapi.monitoring.model.enums.SensorStatus;
import com.groweasy.groweasyapi.monitoring.model.enums.SensorType;
import com.groweasy.groweasyapi.monitoring.repository.DeviceConfigRepository;
import com.groweasy.groweasyapi.monitoring.repository.DeviceDataRepository;
import com.groweasy.groweasyapi.monitoring.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceDataRepository deviceDataRepository;
    private final DeviceConfigRepository deviceConfigRepository;
    private final MetricRepository metricRepository;
    private final AuthService authService;

    public void receiveData(DeviceDataRequest data) {
        Long userId = authService.getAuthenticatedUser().id();

        Sensor sensor = deviceDataRepository.findByUserId(userId)
                .orElse(Sensor.builder()
                        .location("Living Room")
                        .status(SensorStatus.OK)
                        .user(UserEntity.builder().id(userId).build())
                        .build());

        Sensor savedSensor = deviceDataRepository.save(sensor);

        Metric temMetric = createMetric(data.temperature(), "°C", SensorType.TEMPERATURE, savedSensor);
        Metric humMetric = createMetric(data.humidity(), "%", SensorType.HUMIDITY, savedSensor);
        Metric lumMetric = createMetric(data.luminosity(), "lux", SensorType.LIGHT, savedSensor);

        metricRepository.saveAll(List.of(temMetric, humMetric, lumMetric));
    }

    private Metric createMetric(Double value, String unit, SensorType type, Sensor sensor) {
        return Metric.builder()
                .value(value)
                .unit(unit)
                .type(type)
                .timestamp(LocalDateTime.now())
                .sensor(sensor)
                .build();
    }

    public DeviceConfigResponse getConfig() {

        Optional<DeviceConfig> config = deviceConfigRepository.findById(1L);
        return DeviceConfigResponse.fromEntity(config.orElseGet(this::createDefaultConfig));
    }

    public DeviceConfigResponse updateConfig(DeviceConfigRequest config) {

        return DeviceConfigResponse.fromEntity(deviceConfigRepository.save(config.toEntity()));
    }

    private DeviceConfig createDefaultConfig() {
        DeviceConfig defaultConfig = DeviceConfig.builder()
                .sampleInterval(10)
                .tempMin(15.0)
                .tempMax(30.0)
                .tempThreshold(28.0)
                .humMin(40.0)
                .humMax(60.0)
                .humThreshold(55.0)
                .lumMin(300)
                .lumMax(1000)
                .lumThreshold(900)
                .build();
        return deviceConfigRepository.save(defaultConfig);
    }
}
