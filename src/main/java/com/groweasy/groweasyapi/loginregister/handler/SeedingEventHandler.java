package com.groweasy.groweasyapi.loginregister.handler;

import com.groweasy.groweasyapi.loginregister.model.entities.PermissionEntity;
import com.groweasy.groweasyapi.loginregister.model.entities.RoleEntity;
import com.groweasy.groweasyapi.loginregister.model.entities.UserEntity;
import com.groweasy.groweasyapi.loginregister.model.enums.RoleEnum;
import com.groweasy.groweasyapi.loginregister.repository.RoleRepository;
import com.groweasy.groweasyapi.loginregister.repository.UserRepository;
import com.groweasy.groweasyapi.monitoring.model.entities.Sensor;
import com.groweasy.groweasyapi.monitoring.model.enums.SensorStatus;
import com.groweasy.groweasyapi.monitoring.model.enums.SensorType;
import com.groweasy.groweasyapi.monitoring.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class SeedingEventHandler {
    private final UserRepository userPersistence;
    private final RoleRepository rolePersistence;
    private final PasswordEncoder passwordEncoder;
    private final SensorRepository sensorRepository;

    @EventListener
    public void on(ApplicationReadyEvent event) {
        var name = event.getApplicationContext().getId();
        log.info("Starting to seed roles and users for {} at {}", name, new Timestamp(System.currentTimeMillis()));

        PermissionEntity createPermission = PermissionEntity.builder().name("CREATE").build();
        PermissionEntity readPermission = PermissionEntity.builder().name("READ").build();
        PermissionEntity updatePermission = PermissionEntity.builder().name("UPDATE").build();
        PermissionEntity deletePermission = PermissionEntity.builder().name("DELETE").build();

        RoleEntity roleAdmin = RoleEntity.builder()
                .roleName(RoleEnum.ADMIN)
                .permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission))
                .build();

        RoleEntity roleUser = RoleEntity.builder()
                .roleName(RoleEnum.AMATEUR)
                .permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission))
                .build();

        RoleEntity roleGuest = RoleEntity.builder()
                .roleName(RoleEnum.EXPERT)
                .permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission))
                .build();

        rolePersistence.saveAll(Set.of(roleAdmin, roleUser, roleGuest));

        //USUARIO DE PRUEBA
        seedUsers(roleAdmin);

        log.info("Finished seeding roles and users for {} at {}", name, new Timestamp(System.currentTimeMillis()));
    }

    private void seedUsers(RoleEntity roleAdmin) {

        // This is the user that will be created during the seeding process
        UserEntity user = UserEntity.builder()
                .fullName("string")
                .username("string")
                .password(passwordEncoder.encode("string"))
                .roles(Set.of(roleAdmin))
                .build();

        userPersistence.save(user);
    }
}