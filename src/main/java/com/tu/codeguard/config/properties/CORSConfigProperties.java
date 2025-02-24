package com.tu.codeguard.config.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The {@code CORSProperties} class is used to hold the CORS properties taken from the application.yml
 */
@Configuration
@ConfigurationProperties(prefix = "application.cors")
@Getter
@Setter
public class CORSConfigProperties {

    @NotNull
    private String[] allowed;
}
