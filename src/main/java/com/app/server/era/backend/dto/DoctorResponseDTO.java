package com.app.server.era.backend.dto;

import com.app.server.era.backend.models.Notification;
import com.app.server.era.backend.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponseDTO {
    private int id;

    private String lastName;

    private String firstName;

    private String surName;

    private boolean active;
}
