package com.groweasy.groweasyapi.monitoring.repository;

import com.groweasy.groweasyapi.monitoring.model.entities.DeviceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DeviceDataRepository extends JpaRepository<DeviceData, Long> {

    List<DeviceData> findAllByUserId(Long userId);

    Optional<DeviceData> findByUserId(Long userId);

    Optional<DeviceData> findSensorByUserId(Long userId);
}
