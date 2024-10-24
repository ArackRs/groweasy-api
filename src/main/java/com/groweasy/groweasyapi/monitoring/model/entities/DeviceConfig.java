package com.groweasy.groweasyapi.monitoring.model.entities;

import com.groweasy.groweasyapi.loginregister.model.entities.UserEntity;
import com.groweasy.groweasyapi.monitoring.model.dto.request.DeviceConfigRequest;
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

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public static DeviceConfig create(UserEntity user) {

        return DeviceConfig.builder()
                .sampleInterval(10)
                .tempMin(15.0)
                .tempMax(30.0)
                .tempThreshold(28.0)
                .humMin(40.0)
                .humMax(60.0)
                .humThreshold(55.0)
                .lumMin(300)
                .lumMax(1000)
                .lumThreshold(900)
                .user(user)
                .build();
    }

    public void update(DeviceConfigRequest config) {

        this.sampleInterval = config.sampleInterval();
        this.tempMin = config.tempMin();
        this.tempMax = config.tempMax();
        this.tempThreshold = config.tempThreshold();
        this.humMin = config.humMin();
        this.humMax = config.humMax();
        this.humThreshold = config.humThreshold();
        this.lumMin = config.lumMin();
        this.lumMax = config.lumMax();
        this.lumThreshold = config.lumThreshold();
    }
}
