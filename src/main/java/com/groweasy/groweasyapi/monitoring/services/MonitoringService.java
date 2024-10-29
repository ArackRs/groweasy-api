package com.groweasy.groweasyapi.monitoring.services;

import com.groweasy.groweasyapi.loginregister.facade.AuthenticationFacade;
import com.groweasy.groweasyapi.loginregister.model.entities.UserEntity;
import com.groweasy.groweasyapi.loginregister.repository.UserRepository;
import com.groweasy.groweasyapi.loginregister.services.UserService;
import com.groweasy.groweasyapi.monitoring.model.dto.request.DeviceDataRequest;
import com.groweasy.groweasyapi.monitoring.model.dto.response.DeviceConfigResponse;
import com.groweasy.groweasyapi.monitoring.model.entities.DeviceConfig;
import com.groweasy.groweasyapi.monitoring.model.entities.DeviceData;
import com.groweasy.groweasyapi.monitoring.model.entities.Metric;
import com.groweasy.groweasyapi.monitoring.model.entities.Sensor;
import com.groweasy.groweasyapi.monitoring.model.enums.DeviceStatus;
import com.groweasy.groweasyapi.monitoring.model.enums.SensorType;
import com.groweasy.groweasyapi.monitoring.repository.DeviceConfigRepository;
import com.groweasy.groweasyapi.monitoring.repository.DeviceDataRepository;
import com.groweasy.groweasyapi.monitoring.repository.MetricRepository;
import com.groweasy.groweasyapi.monitoring.repository.SensorRepository;
import com.groweasy.groweasyapi.notification.model.enums.NotificationType;
import com.groweasy.groweasyapi.notification.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final DeviceDataRepository deviceDataRepository;
    private final DeviceConfigRepository deviceConfigRepository;
    private final SensorRepository sensorRepository;
    private final MetricRepository metricRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public void receiveData(DeviceDataRequest data) {

        DeviceData deviceData = deviceDataRepository.findByMacAddress(data.macAddress())
                .orElseGet(() -> createDefaultDevice(data.macAddress()));

        DeviceConfig config = deviceConfigRepository.findByDeviceDataId(deviceData.getId())
                .orElseGet(() -> createDefaultConfig(deviceData));

        deviceConfigRepository.save(config);

        if (deviceData.getStatus().equals(DeviceStatus.ACTIVE)) {

            Sensor temSensor = sensorRepository.findByTypeAndDeviceDataId(SensorType.TEMPERATURE, deviceData.getId())
                    .orElseGet(() -> Sensor.create(SensorType.TEMPERATURE, deviceData));
            Metric temMetric = Metric.create(data.temperature(), "°C", temSensor);

            Sensor humSensor = sensorRepository.findByTypeAndDeviceDataId(SensorType.HUMIDITY, deviceData.getId())
                    .orElseGet(() -> Sensor.create(SensorType.HUMIDITY, deviceData));
            Metric humMetric = Metric.create(data.humidity(), "%", humSensor);

            Sensor lumSensor = sensorRepository.findByTypeAndDeviceDataId(SensorType.LUMINOSITY, deviceData.getId())
                    .orElseGet(() -> Sensor.create(SensorType.LUMINOSITY, deviceData));
            Metric lumMetric = Metric.create(data.luminosity(), "lux", lumSensor);

            sensorRepository.saveAll(List.of(temSensor, humSensor, lumSensor));
            metricRepository.saveAll(List.of(temMetric, humMetric, lumMetric));

            checkThresholds(data.temperature(), data.humidity(), data.luminosity(), data.macAddress());
        }
    }

    public DeviceConfigResponse getConfig(String mac) {

        DeviceData deviceData = deviceDataRepository.findByMacAddress(mac)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        DeviceConfig config = deviceConfigRepository.findByDeviceDataId(deviceData.getId())
                .orElseThrow(() -> new RuntimeException("Config not found"));

        return DeviceConfigResponse.fromEntity(config);
    }

    private void checkThresholds(Double temperature, Double humidity, Double luminosity, String mac) {

        DeviceData deviceData = deviceDataRepository.findByMacAddress(mac)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        DeviceConfig config = deviceConfigRepository.findByDeviceDataId(deviceData.getId())
                .orElseThrow(() -> new RuntimeException("Config not found"));

        Long userId = deviceData.getUser().getId();

        // Comprobación de umbrales de temperatura
        if (temperature < config.getTempMin() || temperature > config.getTempMax()) {
            notificationService.createNotification(userId, "Temp fuera de rango!", NotificationType.ALERT);
        } else if (temperature >= config.getTempThreshold()) {
            notificationService.createNotification(userId, "Alerta Temp alta!", NotificationType.ALERT);
        }

        // Comprobación de umbrales de humedad
        if (humidity < config.getHumMin() || humidity > config.getHumMax()) {
            notificationService.createNotification(userId, "Hum fuera de rango!", NotificationType.ALERT);
        } else if (humidity >= config.getHumThreshold()) {
            notificationService.createNotification(userId, "Alerta Hum alta!", NotificationType.ALERT);
        }

        // Comprobación de umbrales de luminosidad
        if (luminosity < config.getLumMin() || luminosity > config.getLumMax()) {
            notificationService.createNotification(userId, "Lum fuera de rango!", NotificationType.ALERT);
        } else if (luminosity >= config.getLumThreshold()) {
            notificationService.createNotification(userId, "Alerta Lum alta!", NotificationType.ALERT);
        }
    }

    private DeviceData createDefaultDevice(String mac) {

        UserEntity user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User admin not found"));

        return deviceDataRepository.save(DeviceData.create(mac, user));
    }
    private DeviceConfig createDefaultConfig(DeviceData deviceData) {

        return deviceConfigRepository.save(DeviceConfig.create(deviceData));
    }
}
