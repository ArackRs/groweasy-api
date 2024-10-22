package com.groweasy.groweasyapi.monitoring.repository;

import com.groweasy.groweasyapi.monitoring.model.entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DeviceDataRepository extends JpaRepository<Sensor, Long> {

    List<Sensor> findAllByUser_Id(Long userId);

    Optional<Sensor> findByUserId(Long userId);
}
