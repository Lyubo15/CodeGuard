package com.tu.codeguard.utils;

import com.tu.codeguard.dbo.ApplicationEntity;
import com.tu.codeguard.dbo.CustomerEntity;
import com.tu.codeguard.dto.Application;
import com.tu.codeguard.dto.ApplicationDTO;
import com.tu.codeguard.dto.Customer;

import java.util.ArrayList;

public class Mapper {

    public static ApplicationEntity mapApplicationToEntity(Application application) {
        return new ApplicationEntity(
                application.id(),
                application.repositoryUrl(),
                application.aiResultFilePath(),
                mapCustomerToEntity(application.customer())
        );
    }

    public static ApplicationDTO mapApplicationToApplicationDTO(ApplicationEntity applicationEntity) {
        return new ApplicationDTO(
                applicationEntity.getId(),
                applicationEntity.getCustomer().getUsername(),
                applicationEntity.getRepositoryUrl()
        );
    }

    public static CustomerEntity mapCustomerToEntity(Customer customer) {
        return new CustomerEntity(
                customer.id(),
                customer.username(),
                new ArrayList<>()
        );
    }

    public static Customer mapCustomerToDomain(CustomerEntity customerEntity) {
        return new Customer(
                customerEntity.getId(),
                customerEntity.getUsername()
        );
    }
}
