package com.tu.codeguard.service.impl;

import com.tu.codeguard.dbo.User;
import com.tu.codeguard.dbo.UserEntity;
import com.tu.codeguard.repository.UserRepository;
import com.tu.codeguard.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Qualifier("authenticationManager") AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User register(String username, String password) {
        List<UserEntity> user = userRepository.findAll();
        UserEntity userEntity;

        if (user.isEmpty()) {
            userEntity = createUser(username, password, "ADMIN");
        } else {
            userEntity = createUser(username, password, "USER");
        }

        log.info("Register user. username: {}", username);
        userRepository.save(userEntity);
        return new User(username);
    }

    private UserEntity createUser(String username, String password, String role) {
        return new UserEntity(
                UUID.randomUUID().toString(),
                username,
                passwordEncoder.encode(password),
                role
        );
    }

    @Override
    public void login(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        log.info("Authentication Successful. username: {}", username);
    }
}
