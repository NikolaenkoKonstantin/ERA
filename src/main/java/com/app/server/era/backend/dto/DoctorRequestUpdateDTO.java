package com.app.server.era.backend.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRequestUpdateDTO {
    private int id;

    @Pattern(regexp = "[А-яA-z]{2,30}", message = "от 2 до 30 символов")
    private String lastName;

    @Pattern(regexp = "[А-яA-z]{2,30}", message = "от 2 до 30 символов")
    private String firstName;

    @Pattern(regexp = "[А-яA-z]{2,30}", message = "от 2 до 30 символов")
    private String surName;
}
