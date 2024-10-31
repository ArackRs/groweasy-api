package com.groweasy.groweasyapi.monitoring.services;

import com.groweasy.groweasyapi.loginregister.facade.AuthenticationFacade;
import com.groweasy.groweasyapi.loginregister.model.entities.UserEntity;
import com.groweasy.groweasyapi.monitoring.model.dto.request.SensorConfigRequest;
import com.groweasy.groweasyapi.monitoring.model.dto.response.MetricResponse;
import com.groweasy.groweasyapi.monitoring.model.dto.response.SensorConfigResponse;
import com.groweasy.groweasyapi.monitoring.model.entities.DeviceConfig;
import com.groweasy.groweasyapi.monitoring.model.entities.Device;
import com.groweasy.groweasyapi.monitoring.model.entities.Metric;
import com.groweasy.groweasyapi.monitoring.model.entities.Sensor;
import com.groweasy.groweasyapi.monitoring.model.enums.DeviceStatus;
import com.groweasy.groweasyapi.monitoring.model.enums.SensorType;
import com.groweasy.groweasyapi.monitoring.repository.DeviceConfigRepository;
import com.groweasy.groweasyapi.monitoring.repository.DeviceRepository;
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

    private final DeviceRepository deviceRepository;
    private final DeviceConfigRepository deviceConfigRepository;
    private final SensorRepository sensorRepository;
    private final MetricRepository metricRepository;
    private final AuthenticationFacade authenticationFacade;

    public void connectDevice(Long id) {

        UserEntity user = authenticationFacade.getCurrentUser();

        Device device = getDeviceById(id);
        device.setUser(user);
        device.setStatus(DeviceStatus.ACTIVE);

        deviceRepository.save(device);
//        createSensors(deviceData);
    }

    public SensorConfigResponse updateConfig(SensorConfigRequest config) {

        UserEntity user = authenticationFacade.getCurrentUser();

        Device device = deviceRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Device not found"));

        DeviceConfig deviceConfig = deviceConfigRepository.findByDeviceId(device.getId())
                .orElseThrow(() -> new RuntimeException("Config not found"));

        deviceConfig.update(config);

        DeviceConfig newConfig = deviceConfigRepository.save(deviceConfig);

        return SensorConfigResponse.fromEntity(newConfig);
    }

    public List<MetricResponse> getMetrics(String mac) {

        Device device = deviceRepository.findByMacAddress(mac)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        List<Sensor> sensors = device.getSensors();

        List<Metric> metrics = metricRepository.findAll();

        metrics.removeIf(metric -> sensors.contains(metric.getSensor()));

        return MetricResponse.fromEntityList(metrics);
    }

    public Device getDeviceById(Long id) {

        return deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
}
