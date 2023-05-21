package com.app.server.era.backend.dto;

import com.app.server.era.backend.models.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDTO {
    private int id;

    private String lastName;

    private String firstName;

    private String surName;

    private int age;

    private String cardNumber;
}
