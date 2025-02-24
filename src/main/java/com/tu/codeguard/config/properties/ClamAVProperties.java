package com.tu.codeguard.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ClamAVProperties configuration properties class
 */
@Configuration
@ConfigurationProperties(prefix = "application.clamav")
@Getter
@Setter
public class ClamAVProperties {

    private boolean enabled;
    private String host;
    private int port;
    private int timeout;
}
