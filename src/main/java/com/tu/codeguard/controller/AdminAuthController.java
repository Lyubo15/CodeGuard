package com.tu.codeguard.controller;

import com.tu.codeguard.dto.User;
import com.tu.codeguard.dto.AuthenticationDTO;
import com.tu.codeguard.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Endpoints handling admin auth - register")
public class AdminAuthController {

    @NonNull
    private final AuthenticationService authenticationService;

    @Operation(summary = "Endpoint to register super admin user")
    @PostMapping("/register")
    public ResponseEntity<User> registerSuperAdmin(
            @RequestBody AuthenticationDTO authenticationDTO
    ) {
        User registeredUser = authenticationService.registerSuperAdmin(
                authenticationDTO.getUsername(), authenticationDTO.getPassword()
        );

        return ResponseEntity.ok(registeredUser);
    }
}
