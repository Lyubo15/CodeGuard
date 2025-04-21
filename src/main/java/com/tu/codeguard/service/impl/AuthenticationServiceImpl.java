package com.tu.codeguard.service.impl;

import com.tu.codeguard.dbo.Role;
import com.tu.codeguard.dto.User;
import com.tu.codeguard.dbo.UserEntity;
import com.tu.codeguard.exceptions.UsernameAlreadyExistsException;
import com.tu.codeguard.repository.UserRepository;
import com.tu.codeguard.service.AuthenticationService;
import com.tu.codeguard.service.RoleService;
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
    private final RoleService roleService;

    public AuthenticationServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Qualifier("authenticationManager") AuthenticationManager authenticationManager, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleService = roleService;
    }

    @Override
    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            log.debug("User already exists");
            throw new UsernameAlreadyExistsException();
        }

        UserEntity userEntity = createUser(username, password, List.of(roleService.getRole("ROLE_USER")));

        userRepository.save(userEntity);
        log.debug("Register user. username: {}", username);
        return new User(
                userEntity.getId(),
                username,
                userEntity.getAuthorities().stream().map(Role::getAuthority).toList()
        );
    }

    @Override
    public User registerSuperAdmin(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            log.debug("User already exists");
            throw new UsernameAlreadyExistsException();
        }

        UserEntity userEntity = createUser(username, password, List.of(roleService.getRole("ROLE_SUPER_ADMIN")));
        userRepository.save(userEntity);
        log.debug("Register super admin user. username: {}", username);
        return new User(
                userEntity.getId(),
                username,
                userEntity.getAuthorities().stream().map(Role::getAuthority).toList()
        );
    }

    private UserEntity createUser(String username, String password, List<Role> roles) {
        return new UserEntity(
                UUID.randomUUID().toString(),
                username,
                passwordEncoder.encode(password),
                roles
        );
    }

    @Override
    public void login(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        log.debug("Authentication Successful. username: {}", username);
    }
}
