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

    private SensorStatus status;

    private String location;

    @OneToMany(mappedBy = "deviceData", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Sensor> sensors = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public static DeviceData create(UserEntity user) {

        return DeviceData.builder()
                .location("Living Room")
                .status(SensorStatus.OK)
                .user(user)
                .build();
    }
}
