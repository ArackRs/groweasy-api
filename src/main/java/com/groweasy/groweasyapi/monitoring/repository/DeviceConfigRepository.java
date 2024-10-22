package com.groweasy.groweasyapi.monitoring.repository;

import com.groweasy.groweasyapi.monitoring.model.entities.DeviceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceConfigRepository extends JpaRepository<DeviceConfig, Long> {
}
