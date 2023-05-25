package com.app.server.era.backend.config;

import com.app.server.era.backend.security.AuthProviderImpl;
import com.app.server.era.backend.services.SecurityService;
import com.app.server.era.ui.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


//Класс конфигурации SpringSecurity
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends VaadinWebSecurity{

    //Конфигурация http
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        //Установка представления входа
        setLoginView(http, LoginView.class);
    }


    //Конфигурация web
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);

        //Игнорирование данных url-адресов (они не защищены)
        web.ignoring().requestMatchers(HttpMethod.GET, "/android/login");
        web.ignoring().requestMatchers(HttpMethod.POST, "/android/dimension");
    }


    //Конфигурация аутентификации
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity,
                                                       AuthProviderImpl authProvider,
                                                       SecurityService userDetailsService) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider)
                .userDetailsService(userDetailsService).and().build();
    }


    //Бин шифратора
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
