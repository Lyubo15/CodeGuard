package com.tu.codeguard.service;

import com.tu.codeguard.dto.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void grantAdminAccess(String userId);

    void revokeAdminAccess(String userId);
}
