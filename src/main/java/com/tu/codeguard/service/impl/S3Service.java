package com.tu.codeguard.service.impl;

import com.tu.codeguard.config.properties.S3Properties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Service {

    @NonNull
    private final S3Client s3Client;

    @NonNull
    private final S3Properties s3Properties;

    public void upload(String key, String content) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .key(key)
                .bucket(s3Properties.getBucket())
                .contentType("text/plain")
                .build();

        RequestBody asyncRequestBody = RequestBody.fromBytes(content.getBytes(UTF_8));
        s3Client.putObject(putRequest, asyncRequestBody);

        log.debug("Upload to S3. Key: {}", key);
    }

    public String downloadTxtFile(String key) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .key(key)
                .bucket(s3Properties.getBucket())
                .build();

        try (ResponseInputStream<GetObjectResponse> objectBytes = s3Client.getObject(objectRequest);
             BufferedReader reader = new BufferedReader(new InputStreamReader(objectBytes, StandardCharsets.UTF_8))) {

            log.debug("Downloading TXT file from S3. Key: {}", key);

            // Read file content into a String
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            return content.toString();
        } catch (IOException e) {
            log.error("Error downloading TXT file from S3: {}", e.getMessage());
            throw new RuntimeException("Failed to download file from S3", e);
        }
    }

    public void delete(String key) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .key(key)
                .bucket(s3Properties.getBucket())
                .build();

        s3Client.deleteObject(deleteRequest);
        log.debug("Deleted file from S3. Key: {}", key);
    }
}
