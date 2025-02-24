package com.tu.codeguard.config;

import com.tu.codeguard.config.properties.BasicAuthProperties;
import com.tu.codeguard.config.properties.CORSConfigProperties;
import com.tu.codeguard.utils.ApiConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static com.tu.codeguard.utils.ApiConstants.ALL;
import static com.tu.codeguard.utils.ApiConstants.INCLUDE_ALL_URL;


@Profile("!test")
@Configuration
@RequiredArgsConstructor
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
            "/actuator/**"
    };

    private final BasicAuthProperties basicAuthProperties;
    private final CORSConfigProperties corsConfigProperties;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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

    @Bean
    public SecurityFilterChain baseFilterChain(HttpSecurity http) throws Exception {
        return httpWithCORSAndNoCSRFStateless(http)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(AUTH_WHITELIST).permitAll()
                                .requestMatchers(SWAGGER_LIST).hasRole(SWAGGER_ROLE)
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .build();

    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        return httpWithCORSAndNoCSRFStateless(http)
                .securityMatcher(ApiConstants.API_URL + ApiConstants.ADMIN_URL + ApiConstants.INCLUDE_ALL_URL)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(AUTH_WHITELIST).permitAll()
                                .anyRequest().hasRole(ADMIN_ROLE)
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    private HttpSecurity httpWithCORSAndNoCSRFStateless(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }
}
