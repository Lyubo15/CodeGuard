package com.tu.codeguard.repository;

import com.tu.codeguard.dbo.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, String> {

    CustomerEntity findByUsername(String username);
}
