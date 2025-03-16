package com.tu.codeguard.config;

import com.tu.codeguard.config.properties.BasicAuthProperties;
import com.tu.codeguard.service.impl.CustomAuthenticationProvider;
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

    @Bean
    protected UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
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

    private UserDetails buildUser(PasswordEncoder encoder, String username, String password, String... roles) {
        return User.builder()
                .passwordEncoder(encoder::encode)
                .username(username)
                .password(password)
                .roles(roles)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        return new ProviderManager(List.of(new CustomAuthenticationProvider(userDetailsService, passwordEncoder())));
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService(passwordEncoder()));
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
