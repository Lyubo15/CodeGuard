package com.tu.codeguard.service.impl;

import com.tu.codeguard.dbo.Role;
import com.tu.codeguard.dbo.UserEntity;
import com.tu.codeguard.dto.User;
import com.tu.codeguard.exceptions.EntityNotFoundException;
import com.tu.codeguard.repository.UserRepository;
import com.tu.codeguard.service.RoleService;
import com.tu.codeguard.service.UserService;
import com.tu.codeguard.utils.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public List<User> getAllUsers() {
        log.info("Getting all users");
        return userRepository
                .findAll()
                .stream()
                .map(Mapper::mapUserToDomain)
                .toList();
    }

    @Override
    public void grantAdminAccess(String userId) {
        log.info("Granting admin access to user {}", userId);
        Optional<UserEntity> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }

        Role adminRole = roleService.getRole("ROLE_ADMIN");

        user.get().getAuthorities().add(adminRole);
        userRepository.save(user.get());
    }

    @Override
    public void revokeAdminAccess(String userId) {
        log.info("Revoking admin access to user {}", userId);
        Optional<UserEntity> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }

        Role adminRole = roleService.getRole("ROLE_ADMIN");

        user.get().getAuthorities().remove(adminRole);
        userRepository.save(user.get());
    }
}
