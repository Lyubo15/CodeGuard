package com.tu.codeguard.service;

import com.tu.codeguard.dto.User;

import java.util.List;

public interface UserService {

    /**
     * Retrieve all users.
     */
    List<User> getAllUsers();

    /**
     * Grant admin access
     *
     * @param userId the user to grant admin access.
     */
    void grantAdminAccess(String userId);

    /**
     * Revoke admin access
     *
     * @param userId the user to revoke admin access.
     */
    void revokeAdminAccess(String userId);
}
