package com.app.server.era.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequestCreateDTO {
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

    @Email(message = "email")
    private String email;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}",
            message = "минимум 6 символов, и хотябы 1 символ каждой картегории: цифра, " +
                    "буква в нижнем регистре, буква в верхнем регистре, спецсимвол из !@#$%^&*")
    private String password;
}
