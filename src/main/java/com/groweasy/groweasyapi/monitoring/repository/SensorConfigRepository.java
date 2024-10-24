package com.groweasy.groweasyapi.monitoring.repository;

import com.groweasy.groweasyapi.monitoring.model.entities.SensorConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorConfigRepository extends JpaRepository<SensorConfig, Long> {
    SensorConfig findBySensorType(String sensorType);
}
