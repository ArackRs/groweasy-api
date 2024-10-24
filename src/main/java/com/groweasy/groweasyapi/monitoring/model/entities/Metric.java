package com.groweasy.groweasyapi.monitoring.model.entities;

import com.groweasy.groweasyapi.monitoring.model.enums.SensorType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "metric_value")
    private Double value;

    @Column
    private String unit;

    @Column
    @Enumerated(EnumType.STRING)
    private SensorType type;

    @ManyToOne
    @JoinColumn(name = "device_data_id")
    private SensorData sensorData;

    @Column
    private LocalDateTime timestamp = LocalDateTime.now();

    public static Metric create(Double value, String unit, SensorType sensorType, SensorData sensorData) {
        return Metric.builder()
                .value(value)
                .unit(unit)
                .type(sensorType)
                .sensorData(sensorData)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PostLoad // Método que se ejecuta después de cargar la entidad
    public void init() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }
}
