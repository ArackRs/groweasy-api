package com.groweasy.groweasyapi.monitoring.services;

import com.groweasy.groweasyapi.loginregister.facade.AuthenticationFacade;
import com.groweasy.groweasyapi.loginregister.model.entities.UserEntity;
import com.groweasy.groweasyapi.monitoring.model.dto.request.DeviceConfigRequest;
import com.groweasy.groweasyapi.monitoring.model.dto.response.DeviceConfigResponse;
import com.groweasy.groweasyapi.monitoring.model.dto.response.MetricResponse;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceDataRepository deviceDataRepository;
    private final DeviceConfigRepository deviceConfigRepository;
    private final SensorRepository sensorRepository;
    private final MetricRepository metricRepository;
    private final AuthenticationFacade authenticationFacade;

    public void connectDevice(Long id) {

        UserEntity user = authenticationFacade.getCurrentUser();

        DeviceData deviceData = getDeviceById(id);
        deviceData.setUser(user);
        deviceData.setStatus(DeviceStatus.ACTIVE);

        deviceDataRepository.save(deviceData);
//        createSensors(deviceData);
    }

    public DeviceConfigResponse updateConfig(DeviceConfigRequest config) {

        UserEntity user = authenticationFacade.getCurrentUser();

        DeviceData deviceData = deviceDataRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Device not found"));

        DeviceConfig deviceConfig = deviceConfigRepository.findByDeviceDataId(deviceData.getId())
                .orElseThrow(() -> new RuntimeException("Config not found"));

        deviceConfig.update(config);

        DeviceConfig newConfig = deviceConfigRepository.save(deviceConfig);

        return DeviceConfigResponse.fromEntity(newConfig);
    }

    public List<MetricResponse> getMetrics(String mac) {

        DeviceData deviceData = deviceDataRepository.findByMacAddress(mac)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        List<Sensor> sensors = deviceData.getSensors();

        List<Metric> metrics = metricRepository.findAll();

        metrics.removeIf(metric -> sensors.contains(metric.getSensor()));

        return MetricResponse.fromEntityList(metrics);
    }

    public DeviceData getDeviceById(Long id) {

        return deviceDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));
    }

    public List<DeviceData> getAllDevices() {
        return deviceDataRepository.findAll();
    }

    private void createSensors(DeviceData deviceData) {
        Sensor temSensor = sensorRepository.save(Sensor.create(SensorType.TEMPERATURE, deviceData));
        Sensor humSensor = sensorRepository.save(Sensor.create(SensorType.HUMIDITY, deviceData));
        Sensor lumSensor = sensorRepository.save(Sensor.create(SensorType.LUMINOSITY, deviceData));

        sensorRepository.saveAll(List.of(temSensor, humSensor, lumSensor));
    }
}
