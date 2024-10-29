package com.groweasy.groweasyapi.monitoring.services;

import com.groweasy.groweasyapi.loginregister.facade.AuthenticationFacade;
import com.groweasy.groweasyapi.loginregister.model.entities.UserEntity;
import com.groweasy.groweasyapi.monitoring.model.dto.request.DeviceConfigRequest;
import com.groweasy.groweasyapi.monitoring.model.dto.response.DeviceConfigResponse;
import com.groweasy.groweasyapi.monitoring.model.dto.response.DeviceDataResponse;
import com.groweasy.groweasyapi.monitoring.model.dto.response.MetricResponse;
import com.groweasy.groweasyapi.monitoring.model.entities.DeviceConfig;
import com.groweasy.groweasyapi.monitoring.model.entities.DeviceData;
import com.groweasy.groweasyapi.monitoring.model.entities.Metric;
import com.groweasy.groweasyapi.monitoring.model.entities.Sensor;
import com.groweasy.groweasyapi.monitoring.repository.DeviceConfigRepository;
import com.groweasy.groweasyapi.monitoring.repository.DeviceDataRepository;
import com.groweasy.groweasyapi.monitoring.repository.MetricRepository;
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
    private final MetricRepository metricRepository;
    private final AuthenticationFacade authenticationFacade;

    public void registerDevice(String serialNumber) {

        UserEntity user = authenticationFacade.getCurrentUser();

        DeviceData deviceData = deviceDataRepository.findBySerialNumber(serialNumber)
                .orElseGet(() -> createDefaultData(serialNumber, user));

        deviceDataRepository.save(deviceData);

        DeviceConfig config = deviceConfigRepository.findByDeviceDataId(deviceData.getId())
                .orElseGet(() -> createDefaultConfig(deviceData));

        deviceConfigRepository.save(config);
    }

    public DeviceConfigResponse updateConfig(String serialNumber, DeviceConfigRequest config) {

        DeviceData deviceData = getDeviceBySerialNumber(serialNumber);

        DeviceConfig deviceConfig = deviceConfigRepository.findByDeviceDataId(deviceData.getId())
                .orElseThrow(() -> new RuntimeException("Config not found"));

        deviceConfig.update(config);

        DeviceConfig newConfig = deviceConfigRepository.save(deviceConfig);

        return DeviceConfigResponse.fromEntity(newConfig);
    }

    public List<MetricResponse> getMetrics(String serialNumber) {

        DeviceData deviceData = deviceDataRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        List<Sensor> sensors = deviceData.getSensors();

        List<Metric> metrics = metricRepository.findAll();

        metrics.removeIf(metric -> sensors.contains(metric.getSensor()));

        return MetricResponse.fromEntityList(metrics);
    }

    public DeviceData getDeviceBySerialNumber(String serialNumber) {

        return deviceDataRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new RuntimeException("Device not found"));
    }

    public List<DeviceData> getAllDevices() {
        return deviceDataRepository.findAll();
    }

    private DeviceData createDefaultData(String serialNumber, UserEntity user) {

        return deviceDataRepository.save(DeviceData.create(serialNumber, user));
    }

    private DeviceConfig createDefaultConfig(DeviceData deviceData) {

        return deviceConfigRepository.save(DeviceConfig.create(deviceData));
    }
}
