package com.app.server.era.backend.services;

import com.app.server.era.backend.models.Doctor;
import com.app.server.era.backend.models.User;
import com.app.server.era.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public User registratePatient(User user) {
        user.setRole("ROLE_PATIENT");
        user.setActive(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }


    @Transactional
    public User registrateDoctor(User user) {
        user.setRole("ROLE_DOCTOR");
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

}
