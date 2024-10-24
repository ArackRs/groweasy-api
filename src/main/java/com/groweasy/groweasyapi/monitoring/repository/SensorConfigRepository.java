package com.groweasy.groweasyapi.monitoring.repository;

import com.groweasy.groweasyapi.monitoring.model.entities.SensorConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SensorConfigRepository extends JpaRepository<SensorConfig, Long> {
    Optional<SensorConfig> findByUserId(Long userId);
}
