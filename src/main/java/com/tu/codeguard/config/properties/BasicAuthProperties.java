package com.tu.codeguard.config.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The {@code CORSProperties} class is used to hold the CORS properties taken from the application.yml
 */
@Configuration
@ConfigurationProperties(prefix = "application.basicauth")
@Getter
@Setter
public class BasicAuthProperties {

    private final Swagger swagger = new Swagger();

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Getter
    @Setter
    public static class Swagger {

        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }
}
