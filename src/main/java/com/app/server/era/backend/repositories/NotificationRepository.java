package com.app.server.era.backend.repositories;

import com.app.server.era.backend.models.Doctor;
import com.app.server.era.backend.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findAllByDoctor(Doctor doctor);
}
