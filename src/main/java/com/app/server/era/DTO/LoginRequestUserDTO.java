package com.app.server.era.DTO;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestUserDTO {
    @Pattern(regexp = "\\w{5,20}", message = "Login must contain at least 5 and no more than 20 characters")
    private String login;

    @Pattern(regexp = "\\w{5,20}", message = "Password must contain at least 5 and no more than 20 characters")
    private String password;
}
