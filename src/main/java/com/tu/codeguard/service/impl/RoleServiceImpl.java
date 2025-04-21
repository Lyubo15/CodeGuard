package com.tu.codeguard.service.impl;

import com.tu.codeguard.dbo.Role;
import com.tu.codeguard.repository.RoleJpaRepository;
import com.tu.codeguard.service.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleJpaRepository roleJpaRepository;

    @PostConstruct
    private void seedRoles() {
        log.debug("Seeding roles");
        if (roleJpaRepository.findAll().isEmpty()) {
            roleJpaRepository.saveAll(
                    new ArrayList<>() {{
                        add(new Role(UUID.randomUUID().toString(), "ROLE_SUPER_ADMIN"));
                        add(new Role(UUID.randomUUID().toString(), "ROLE_ADMIN"));
                        add(new Role(UUID.randomUUID().toString(), "ROLE_USER"));
                    }});
        }
    }

    @Override
    public Role getRole(String role) {
        log.debug("Getting role {}", role);
        return roleJpaRepository
                .findAll()
                .stream()
                .filter(r -> r.getAuthority().toLowerCase().contains(role.toLowerCase()))
                .findFirst()
                .orElse(new Role(UUID.randomUUID().toString(), "ROLE_USER"));
    }
}
