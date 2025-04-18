package com.tu.codeguard.controller;

import com.tu.codeguard.dto.AuthenticationDTO;
import com.tu.codeguard.dto.User;
import com.tu.codeguard.service.AuthenticationService;
import com.tu.codeguard.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Endpoints handling public auth - login")
public class PublicAuthController {

    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public PublicAuthController(
            AuthenticationService authenticationService,
            JwtUtil jwtUtil,
            @Qualifier("databaseUserDetailsService") UserDetailsService userDetailsService) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Operation(summary = "Endpoint login the user")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody AuthenticationDTO authenticationDTO
    ) {
        authenticationService.login(authenticationDTO.getUsername(), authenticationDTO.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDTO.getUsername());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = jwtUtil.generateToken(userDetails.getUsername(), roles);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Endpoint to register the user")
    @PostMapping("/register")
    public ResponseEntity<User> register(
            @RequestBody AuthenticationDTO authenticationDTO
    ) {
        User registeredUser = authenticationService.register(
                authenticationDTO.getUsername(), authenticationDTO.getPassword()
        );

        return ResponseEntity.ok(registeredUser);
    }
}
