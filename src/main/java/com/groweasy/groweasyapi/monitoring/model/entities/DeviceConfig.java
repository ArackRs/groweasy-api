package com.groweasy.groweasyapi.monitoring.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table
public class DeviceConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int sampleInterval;  // Intervalo de muestreo en segundos
    @Column(nullable = false)
    private double tempMin;  // Mínimo de temperatura permitida
    @Column(nullable = false)
    private double tempMax;  // Máximo de temperatura permitida
    @Column(nullable = false)
    private double tempThreshold;  // Umbral de alerta de temperatura alta
    @Column(nullable = false)
    private double humMin;  // Mínimo de humedad permitida
    @Column(nullable = false)
    private double humMax;  // Máximo de humedad permitida
    @Column(nullable = false)
    private double humThreshold;  // Umbral de alerta de humedad alta
    @Column(nullable = false)
    private int lumMin;  // Mínimo de luminosidad permitida
    @Column(nullable = false)
    private int lumMax;  // Máximo de luminosidad permitida
    @Column(nullable = false)
    private int lumThreshold;  // Umbral de alerta de luminosidad alta
}
