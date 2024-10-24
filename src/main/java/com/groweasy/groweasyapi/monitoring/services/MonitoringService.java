package com.groweasy.groweasyapi.monitoring.services;

import com.groweasy.groweasyapi.monitoring.model.entities.SensorConfig;
import com.groweasy.groweasyapi.monitoring.model.entities.SensorData;
import com.groweasy.groweasyapi.monitoring.repository.SensorConfigRepository;
import com.groweasy.groweasyapi.monitoring.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitoringService {

    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Autowired
    private SensorConfigRepository sensorConfigRepository;

    public List<SensorData> getAllSensorData() {
        return sensorDataRepository.findAll();
    }

    public SensorData saveSensorData(SensorData sensorData) {
        return sensorDataRepository.save(sensorData);
    }

    public String checkThresholds() {
        List<SensorData> sensorDataList = sensorDataRepository.findAll();
        StringBuilder alertas = new StringBuilder();

        for (SensorData sensorData : sensorDataList) {
            SensorConfig config = sensorConfigRepository.findBySensorType(sensorData.getSensorType().toLowerCase());
            if (config == null) {
                alertas.append("ALERTA: ¡No se encontró configuración para el tipo de sensor!\n");
                continue;
            }

            if (sensorData.getValue() > config.getMaxThreshold()) {
                alertas.append("ALERTA: ¡" + sensorData.getSensorType() + " excede el umbral máximo!\n");
            } else if (sensorData.getValue() < config.getMinThreshold()) {
                alertas.append("ALERTA: ¡" + sensorData.getSensorType() + " está por debajo del umbral mínimo!\n");
            }
        }
        return alertas.toString().isEmpty() ? "Todos los valores de los sensores están dentro del rango normal." : alertas.toString();
    }
}
