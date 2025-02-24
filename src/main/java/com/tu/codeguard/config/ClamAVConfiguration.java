package com.tu.codeguard.config;

import com.tu.codeguard.config.properties.ClamAVProperties;
import fi.solita.clamav.ClamAVClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClamAVConfiguration - Configuration class in order to create a ClamAvClient bean.
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class ClamAVConfiguration {

    private final ClamAVProperties clamAVProperties;

    /**
     * Method that creates a ClamAVClient bean, uses preconfigured host and port in order to establish connection
     *
     * @return ClamAVClient
     */
    @Bean
    public ClamAVClient getClamAVClient() {
        return new ClamAVClient(clamAVProperties.getHost(), clamAVProperties.getPort(), clamAVProperties.getTimeout());
    }
}
