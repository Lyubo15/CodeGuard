package com.tu.codeguard.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * The {@code AWSProperties} class is used to hold the application related properties taken from the application.yml
 */
@Configuration
@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class AWSProperties {

    private final Aws aws = new Aws();

    @Getter
    @Setter
    public static class Aws {

        @NotBlank
        private String accessKey;

        @NotBlank
        private String secretAccessKey;
    }
}
