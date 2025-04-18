package com.tu.codeguard.controller;

import com.tu.codeguard.dto.User;
import com.tu.codeguard.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Endpoints handling public operations for Users")
public class PublicUserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Endpoint to retrieve all users")
    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }
}
