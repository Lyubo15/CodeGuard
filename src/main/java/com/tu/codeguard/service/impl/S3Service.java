package com.tu.codeguard.service;

import com.tu.codeguard.config.properties.S3Properties;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.zip.ZipInputStream;

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
                .bucket(s3Properties.getBucket())
                .key(key)
                .contentType("text/plain")
                .build();

        RequestBody asyncRequestBody = RequestBody.fromBytes(content.getBytes(UTF_8));
        s3Client.putObject(putRequest, asyncRequestBody);

        log.info("Upload to S3. Key: {}", key);
    }

    public ZipInputStream download(String key) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
            .key(key)
            .bucket(s3Properties.getBucket())
            .build();

        ResponseInputStream<GetObjectResponse> objectBytes = s3Client.getObject(objectRequest);

        log.info("Download from S3. Key: {}", key);
        return new ZipInputStream(objectBytes);
    }
}
