package com.tu.codeguard.repository;

import com.tu.codeguard.dbo.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<Role, String> {
}
