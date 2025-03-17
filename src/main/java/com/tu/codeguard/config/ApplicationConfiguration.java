package com.tu.codeguard.config;

import com.tu.codeguard.config.properties.BasicAuthProperties;
import com.tu.codeguard.repository.UserRepository;
import com.tu.codeguard.service.impl.CustomAuthenticationProvider;
import com.tu.codeguard.service.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private static final String SWAGGER_ROLE = "SWAGGER";
    private static final String ADMIN_ROLE = "ADMIN";

    private final BasicAuthProperties basicAuthProperties;

    /**
     * In-Memory Users for Admin Authentication (Swagger / Admin Panel)
     */
    @Bean
    protected UserDetailsService swaggerUserDetailsService(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        UserDetails adminUser = buildUser(passwordEncoder,
                basicAuthProperties.getUsername(),
                basicAuthProperties.getPassword(),
                ADMIN_ROLE);

        UserDetails swaggerUser = buildUser(passwordEncoder,
                basicAuthProperties.getSwagger().getUsername(),
                basicAuthProperties.getSwagger().getPassword(),
                SWAGGER_ROLE);

        manager.createUser(adminUser);
        manager.createUser(swaggerUser);

        return manager;
    }

    /**
     * Database Users for Public API Authentication (JWT-based)
     */
    @Bean
    public UserDetailsService databaseUserDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    private UserDetails buildUser(PasswordEncoder encoder, String username, String password, String... roles) {
        return User.builder()
                .passwordEncoder(encoder::encode)
                .username(username)
                .password(password)
                .roles(roles)
                .build();
    }

    /**
     * Authentication Provider for Admin (Basic Auth)
     */
    @Bean
    public AuthenticationProvider adminAuthProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(swaggerUserDetailsService(passwordEncoder));
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * Authentication Provider for Public API (JWT-based)
     */
    @Bean
    public AuthenticationProvider publicAuthProvider(UserRepository userRepository) {
        return new CustomAuthenticationProvider(databaseUserDetailsService(userRepository), passwordEncoder());
    }

    /**
     * AuthenticationManager that includes both providers
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider adminAuthProvider, AuthenticationProvider publicAuthProvider) {
        return new ProviderManager(List.of(adminAuthProvider, publicAuthProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
