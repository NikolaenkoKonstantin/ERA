package com.app.server.era.backend.services;

import com.app.server.era.backend.models.User;
import com.app.server.era.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Сервис регистрации
@Service
@RequiredArgsConstructor
public class RegistrationService {
    //Репозиторий модели user
    private final UserRepository userRepo;
    //Шифратор
    private final PasswordEncoder passwordEncoder;


    //Зарегистрировать пациента
    @Transactional
    public User registratePatient(User user) {
        //Конфигурация пользователя
        user.setRole("ROLE_PATIENT");
        user.setActive(false);
        user.setPassword(
                passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }


    //Зарегистрировать врача
    @Transactional
    public User registrateDoctor(User user) {
        //Конфигурация пользователя
        user.setRole("ROLE_DOCTOR");
        user.setActive(true);
        user.setPassword(
                passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
