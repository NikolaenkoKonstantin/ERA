package com.app.server.era.backend.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordEditRequest {
    private int id;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}",
            message = "минимум 6 символов, и хотябы 1 символ каждой картегории: цифра, " +
                    "буква в нижнем регистре, буква в верхнем регистре, спецсимвол из !@#$%^&*")
    private String password;

    public PasswordEditRequest(int id) {
        this.id = id;
    }
}
