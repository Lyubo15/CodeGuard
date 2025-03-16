package com.tu.codeguard.repository;

import com.tu.codeguard.dbo.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationJpaRepository extends JpaRepository<ApplicationEntity, String> {
}
