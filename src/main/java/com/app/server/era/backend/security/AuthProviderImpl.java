package com.app.server.era.backend.security;

import com.app.server.era.backend.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Component
@RequiredArgsConstructor
public class AuthProviderImpl implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //Поиск аккаунта пользователя
        UserDetails userDetails = securityService.loadUserByUsername(authentication.getName());

        String password = authentication.getCredentials().toString();

        //Проверка пароля
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }
        //Проверка аккаунта на активность (заморожен или нет)
        else if(!((UserDetailsImpl) userDetails).getUser().isActive()){
            throw new BadCredentialsException("account is frozen");
        }

        //Отправка данных в principal
        return new UsernamePasswordAuthenticationToken(userDetails, password,
                userDetails.getAuthorities());
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
