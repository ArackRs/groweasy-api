package com.groweasy.groweasyapi.monitoring.repository;

import com.groweasy.groweasyapi.monitoring.model.entities.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

    List<SensorData> findAllByUser_Id(Long userId);

    Optional<SensorData> findByUserId(Long userId);
}
