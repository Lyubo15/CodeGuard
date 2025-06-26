package com.tu.codeguard.config;

import com.tu.codeguard.config.properties.CORSConfigProperties;
import com.tu.codeguard.filter.JwtAuthenticationFilter;
import com.tu.codeguard.utils.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static com.tu.codeguard.utils.ApiConstants.ALL;
import static com.tu.codeguard.utils.ApiConstants.INCLUDE_ALL_URL;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private static final String SWAGGER_ROLE = "SWAGGER";
    private static final String ADMIN_ROLE = "ADMIN";

    private static final String[] SWAGGER_LIST = {
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger"
    };
    private static final String[] AUTH_WHITELIST = {
            "/actuator/**",
            "/api/public/auth/login", // Allow JWT login without authentication
            "/api/public/auth/test",
    };

    private final CORSConfigProperties corsConfigProperties;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        return httpWithCORSAndNoCSRFStateless(http)
                .securityMatcher(SWAGGER_LIST)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().hasRole(SWAGGER_ROLE)
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public SecurityFilterChain adminFilterChain(HttpSecurity http, AuthenticationProvider adminAuthProvider) throws Exception {
        return httpWithCORSAndNoCSRFStateless(http)
                .securityMatcher(ApiConstants.API_URL + ApiConstants.ADMIN_URL + ApiConstants.INCLUDE_ALL_URL)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(AUTH_WHITELIST).permitAll()
                                .anyRequest().hasRole(ADMIN_ROLE)
                )
                .authenticationProvider(adminAuthProvider)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public SecurityFilterChain publicFilterChain(HttpSecurity http, AuthenticationProvider publicAuthProvider) throws Exception {
        return httpWithCORSAndNoCSRFStateless(http)
                .securityMatcher(ApiConstants.API_URL + ApiConstants.PUBLIC_URL + ApiConstants.INCLUDE_ALL_URL)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(publicAuthProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        if (corsConfigProperties.getAllowed() != null) {
            configuration.setAllowedOrigins(Arrays.asList(corsConfigProperties.getAllowed()));
        }
        configuration.setAllowedMethods(List.of(ALL));
        configuration.setAllowedHeaders(List.of(ALL));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(INCLUDE_ALL_URL, configuration);
        return source;
    }

    private HttpSecurity httpWithCORSAndNoCSRFStateless(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }
}
