package com.tu.codeguard.service;

import com.tu.codeguard.dto.User;

public interface AuthenticationService {

    /**
     * Register new user
     *
     * @param username the username.
     * @param password the password
     */
    User register(String username, String password);

    /**
     * Login new user
     *
     * @param username the username.
     * @param password the password
     */
    void login(String username, String password);

}
