package com.tu.codeguard.service;

import com.tu.codeguard.dbo.Role;

public interface RoleService {

    /**
     * Retrieve role.
     *
     * @param role the role name.
     */
    Role getRole(String role);
}
