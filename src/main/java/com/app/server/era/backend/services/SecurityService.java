package com.app.server.era.backend.services;

import com.app.server.era.backend.models.User;
import com.app.server.era.backend.repositories.UserRepository;
import com.app.server.era.backend.security.UserDetailsImpl;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {
    private final UserRepository userRepo;
    private final AuthenticationContext authenticationContext;


    //проверка на существование аккаунта
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByLogin(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found!");

        return new UserDetailsImpl(user.get());
    }


    public void logout(){
        authenticationContext.logout();
    }
}
