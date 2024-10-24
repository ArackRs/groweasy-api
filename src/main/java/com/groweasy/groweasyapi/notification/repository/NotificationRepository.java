package com.groweasy.groweasyapi.notification.repository;

import com.groweasy.groweasyapi.notification.model.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Métodos adicionales personalizados si son necesarios
}
