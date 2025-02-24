package com.tu.codeguard.config.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.openai")
@Getter
@Setter
public class OpenAIProperties {

    @NotBlank
    private String token;

    @NotBlank
    private String model;

    @NotBlank
    private Long connectionDuration;

    @NotBlank
    private Integer maxOutputTokens;

    @NotBlank
    private Integer maxTokens;
}
