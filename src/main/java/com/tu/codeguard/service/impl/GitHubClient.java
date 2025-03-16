package com.tu.codeguard.service.impl;

import com.tu.codeguard.config.properties.GitHubApiProperties;
import com.tu.codeguard.exceptions.GithubExportException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

@Slf4j
@Component
public class GitHubClient {

    private final RestTemplate restTemplate;
    private final GitHubApiProperties gitHubApiProperties;

    public GitHubClient(RestTemplate restTemplate, GitHubApiProperties gitHubApiProperties) {
        this.restTemplate = restTemplate;
        this.gitHubApiProperties = gitHubApiProperties;
    }

    public byte[] exportRepository(String owner, String repo) throws GithubExportException {
        String repoPath = String.format("%s/repos/%s/%s/zipball", gitHubApiProperties.getBaseUrl(), owner, repo);
        URI exportProjectURI = UriComponentsBuilder.fromUriString(repoPath).build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github+json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(exportProjectURI, HttpMethod.GET, requestEntity, byte[].class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.error("Github export succeed. owner: {}, repo: {}", owner, repo);
                return response.getBody();
            } else {
                log.error("Github export failed. reason: {}, owner: {}, repo: {}", response.getBody(), owner, repo);
                throw new GithubExportException("Failed to export repository" + repo);
            }
        } catch (Exception e) {
            log.error("Github export failed. reason: {}, owner: {}, repo: {}", e.getMessage(), owner, repo);
            throw new GithubExportException("Failed to export repository" + repo);
        }
    }

    private String[] extractOwnerAndRepo(String repositoryUrl) {
        try {
            URI uri = URI.create(repositoryUrl);
            String[] pathSegments = uri.getPath().split("/");
            if (pathSegments.length >= 3) {
                return new String[]{pathSegments[1], pathSegments[2]}; // [username, repository]
            }
        } catch (Exception e) {
            log.error("Error extracting owner and repo from URL: {}", repositoryUrl, e);
        }
        return null;
    }
}
