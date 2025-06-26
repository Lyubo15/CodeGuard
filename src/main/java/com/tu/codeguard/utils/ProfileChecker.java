package com.tu.codeguard.utils;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ProfileChecker {

    private final Environment environment;

    public ProfileChecker(Environment environment) {
        this.environment = environment;
    }

    public boolean isProd() {
        return Arrays.asList(environment.getActiveProfiles()).contains("prod");
    }
}

