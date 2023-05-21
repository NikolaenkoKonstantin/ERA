package com.app.server.era.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequestUpdateDTO {
    private int id;

    @Pattern(regexp = "[А-яA-z]{2,30}", message = "от 2 до 30 символов")
    private String lastName;

    @Pattern(regexp = "[А-яA-z]{2,30}", message = "от 2 до 30 символов")
    private String firstName;

    @Pattern(regexp = "[А-яA-z]{2,30}", message = "от 2 до 30 символов")
    private String surName;

    @Min(value = 0, message = "не может быть меньше 0")
    @Max(value = 100, message = "не может быть больше 100")
    private int age;

    @Pattern(regexp = "\\d{6}", message = "6 цифр")
    private String cardNumber;
}
