package com.tu.codeguard.service.impl;

import com.tu.codeguard.dbo.CustomerEntity;
import com.tu.codeguard.dto.Customer;
import com.tu.codeguard.repository.CustomerJpaRepository;
import com.tu.codeguard.service.CustomerService;
import com.tu.codeguard.utils.Mapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    @NonNull
    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public Customer getCustomer(String username) {
        CustomerEntity customer = customerJpaRepository.findByUsername(username);
        if (customer == null) {
            return null;
        }

        log.info("Customer found with name {}", customer.getUsername());
        return Mapper.mapCustomerToDomain(customer);
    }

    @Override
    public Customer saveCustomer(String username) {
        CustomerEntity customerEntity = new CustomerEntity(UUID.randomUUID().toString(), username, new ArrayList<>());
        customerJpaRepository.save(customerEntity);

        log.info("Customer saved with name {}", customerEntity.getUsername());
        return Mapper.mapCustomerToDomain(customerEntity);
    }
}
