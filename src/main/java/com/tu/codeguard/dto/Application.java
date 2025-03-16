package com.tu.codeguard.dto;

public record Application(
        String id,
        String repositoryUrl,
        String aiResultFilePath,
        Customer customer
) { }
