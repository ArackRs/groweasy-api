package com.groweasy.groweasyapi.monitoring.model.entities;

import com.groweasy.groweasyapi.loginregister.model.entities.UserEntity;
import com.groweasy.groweasyapi.monitoring.model.enums.DeviceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table
public class DeviceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String macAddress;

    private DeviceStatus status;

    private String location;

    @OneToMany(mappedBy = "deviceData", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Sensor> sensors = new ArrayList<>();

    @OneToOne(mappedBy = "deviceData", cascade = CascadeType.ALL)
    private DeviceConfig deviceConfig;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity user;

    public static DeviceData create(String mac, UserEntity user) {

        return DeviceData.builder()
                .macAddress(mac)
                .location("Living Room")
                .status(DeviceStatus.INACTIVE)
                .user(user)
                .build();
    }
}
