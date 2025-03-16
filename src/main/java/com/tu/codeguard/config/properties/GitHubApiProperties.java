package com.tu.codeguard.config.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.githubapi")
@Getter
@Setter
public class GitHubApiProperties {

    @NotBlank
    private String baseUrl;
}