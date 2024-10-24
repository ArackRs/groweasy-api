package com.groweasy.groweasyapi.monitoring.services;

import com.groweasy.groweasyapi.loginregister.model.dto.response.UserResponse;
import com.groweasy.groweasyapi.loginregister.model.entities.UserEntity;
import com.groweasy.groweasyapi.loginregister.services.AuthService;
import com.groweasy.groweasyapi.monitoring.model.dto.request.DeviceConfigRequest;
import com.groweasy.groweasyapi.monitoring.model.dto.request.DeviceDataRequest;
import com.groweasy.groweasyapi.monitoring.model.dto.response.DeviceConfigResponse;
import com.groweasy.groweasyapi.monitoring.model.entities.SensorConfig;
import com.groweasy.groweasyapi.monitoring.model.entities.SensorData;
import com.groweasy.groweasyapi.monitoring.model.entities.Metric;
import com.groweasy.groweasyapi.monitoring.model.enums.SensorType;
import com.groweasy.groweasyapi.monitoring.repository.SensorConfigRepository;
import com.groweasy.groweasyapi.monitoring.repository.SensorDataRepository;
import com.groweasy.groweasyapi.monitoring.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final SensorDataRepository sensorDataRepository;
    private final SensorConfigRepository sensorConfigRepository;
    private final MetricRepository metricRepository;
    private final AuthService authService;

    public void receiveData(DeviceDataRequest data) {
        Long userId = authService.getAuthenticatedUser().id();

        SensorData sensorData = sensorDataRepository.findByUserId(userId)
                .orElse(SensorData.create(userId));

        SensorData savedSensorData = sensorDataRepository.save(sensorData);

        // Crea métricas para temperatura, humedad y luminosidad
        Metric temMetric = Metric.create(data.temperature(), "°C", SensorType.TEMPERATURE, savedSensorData);
        Metric humMetric = Metric.create(data.humidity(), "%", SensorType.HUMIDITY, savedSensorData);
        Metric lumMetric = Metric.create(data.luminosity(), "lux", SensorType.LUMINOSITY, savedSensorData);

        metricRepository.saveAll(List.of(temMetric, humMetric, lumMetric));

        // Verifica los umbrales después de guardar los datos
        SensorConfig sensorConfig = sensorConfigRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Config not found"));

        checkThresholds(data.temperature(), data.humidity(), data.luminosity(), sensorConfig);
    }

    public DeviceConfigResponse getConfig() {

        Long userId = authService.getAuthenticatedUser().id();

        SensorConfig config = sensorConfigRepository.findByUserId(userId)
                .orElseGet(this::createDefaultConfig);

        return DeviceConfigResponse.fromEntity(config);
    }

    public DeviceConfigResponse updateConfig(DeviceConfigRequest config) {

        Long userId = authService.getAuthenticatedUser().id();

        SensorConfig sensorConfig = sensorConfigRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Config not found"));

        sensorConfig.update(config);

        SensorConfig newConfig = sensorConfigRepository.save(sensorConfig);

        return DeviceConfigResponse.fromEntity(newConfig);
    }

    private void checkThresholds(Double temperature, Double humidity, Double luminosity, SensorConfig config) {
        // Comprobación de umbrales de temperatura
        if (temperature < config.getTempMin() || temperature > config.getTempMax()) {
            System.out.println("Temp fuera de rango!");
        } else if (temperature >= config.getTempThreshold()) {
            System.out.println("Alerta Temp alta!");
        }

        // Comprobación de umbrales de humedad
        if (humidity < config.getHumMin() || humidity > config.getHumMax()) {
            System.out.println("Hum fuera de rango!");
        } else if (humidity >= config.getHumThreshold()) {
            System.out.println("Alerta Hum alta!");
        }

        // Comprobación de umbrales de luminosidad
        if (luminosity < config.getLumMin() || luminosity > config.getLumMax()) {
            System.out.println("Lum fuera de rango!");
        } else if (luminosity >= config.getLumThreshold()) {
            System.out.println("Alerta Lum alta!");
        }
    }

    private SensorConfig createDefaultConfig() {

        UserEntity user = UserResponse.toEntity(authService.getAuthenticatedUser());

        return sensorConfigRepository.save(SensorConfig.createDefaultConfig(user));
    }
}
