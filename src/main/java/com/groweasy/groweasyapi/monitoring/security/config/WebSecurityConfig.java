package com.groweasy.monitoring.services;

import com.groweasy.monitoring.model.SensorData;
import com.groweasy.monitoring.repositories.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitoringService {

    @Autowired
    private SensorDataRepository sensorDataRepository;

    public List<SensorData> getAllSensorData() {
        return sensorDataRepository.findAll();
    }

    public SensorData saveSensorData(SensorData sensorData) {
        return sensorDataRepository.save(sensorData);
    }

    public void deleteSensorData(Long id) {
        sensorDataRepository.deleteById(id);
    }
}
