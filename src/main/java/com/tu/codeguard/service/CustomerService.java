package com.tu.codeguard.service;

import com.tu.codeguard.dto.Customer;

public interface CustomerService {

    /**
     * Retrieve customer
     *
     * @param username the unique name of the user.
     */
    Customer getCustomer(String username);

    /**
     * Save customer
     *
     * @param username the unique name of the user.
     */
    Customer saveCustomer(String username);

}
