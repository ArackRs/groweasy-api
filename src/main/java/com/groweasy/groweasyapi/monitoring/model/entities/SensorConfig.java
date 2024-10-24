package com.groweasy.groweasyapi.monitoring.model.entities;

import javax.persistence.*;

@Entity
public class SensorConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sensorType;
    private Double minThreshold;
    private Double maxThreshold;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public Double getMinThreshold() {
        return minThreshold;
    }

    public void setMinThreshold(Double minThreshold) {
        this.minThreshold = minThreshold;
    }

    public Double getMaxThreshold() {
        return maxThreshold;
    }

    public void setMaxThreshold(Double maxThreshold) {
        this.maxThreshold = maxThreshold;
    }
}
