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
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

//    private SensorType type;

    private SensorStatus status;

    @OneToMany(mappedBy = "sensorData", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Metric> metrics = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public static SensorData create(Long userId) {

        return SensorData.builder()
                .location("Living Room")
                .status(SensorStatus.OK)
                .user(UserEntity.builder().id(userId).build())
                .build();
    }
}
