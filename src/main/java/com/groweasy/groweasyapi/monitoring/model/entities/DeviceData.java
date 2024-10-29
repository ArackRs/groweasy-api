package com.groweasy.groweasyapi.monitoring.model.entities;

import com.groweasy.groweasyapi.loginregister.model.entities.UserEntity;
import com.groweasy.groweasyapi.monitoring.model.enums.SensorStatus;
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
    private String name;

    private SensorStatus status;

    private String location;

    @OneToMany(mappedBy = "deviceData", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Sensor> sensors = new ArrayList<>();

    @OneToOne(mappedBy = "deviceData", cascade = CascadeType.ALL)
    private DeviceConfig deviceConfig;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity user;

    public static DeviceData create(String name, UserEntity user) {

        return DeviceData.builder()
                .name(name)
                .location("Living Room")
                .status(SensorStatus.OK)
                .user(user)
                .build();
    }
}
