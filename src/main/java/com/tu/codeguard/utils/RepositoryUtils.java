package com.tu.codeguard.utils;

import com.tu.codeguard.exceptions.ExtractRepositoryComponentsException;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Slf4j
public class RepositoryUtils {

    public static String[] extractOwnerAndRepo(String repositoryUrl) {
        try {
            URI uri = URI.create(repositoryUrl);
            String[] pathSegments = uri.getPath().split("/");
            if (pathSegments.length >= 3) {
                return new String[]{pathSegments[1], pathSegments[2]}; // [username, repository]
            }
        } catch (Exception e) {
            log.error("Error extracting owner and repo from URL: {}", repositoryUrl, e);
            throw new ExtractRepositoryComponentsException("Error extracting owner and repo from URL");
        }

        throw new IllegalArgumentException("Invalid GitHub repository URL: " + repositoryUrl);
    }
}
