package com.app.server.era.backend.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRequestCreateDTO {
    @Pattern(regexp = "[А-яA-z]{2,30}", message = "от 2 до 30 символов")
    private String lastName;

    @Pattern(regexp = "[А-яA-z]{2,30}", message = "от 2 до 30 символов")
    private String firstName;

    @Pattern(regexp = "[А-яA-z]{2,30}", message = "от 2 до 30 символов")
    private String surName;

    @Size(min = 6, max = 30, message = "от 6 до 30 символов")
    private String login;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}",
            message = "минимум 6 символов, и хотябы 1 символ каждой картегории: цифра, " +
                    "буква в нижнем регистре, буква в верхнем регистре, спецсимвол из !@#$%^&*")
    private String password;
}
