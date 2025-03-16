package com.tu.codeguard.controller;

import com.tu.codeguard.service.AuthenticationService;
import com.tu.codeguard.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Endpoints handling public auth - login")
public class PublicAuthController {

    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public PublicAuthController(AuthenticationService authenticationService, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Operation(summary = "Endpoint login the user")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        authenticationService.login(username, password);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(Map.of("token", token));
    }
}
