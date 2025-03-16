package com.tu.codeguard.config;

import java.time.Duration;

import com.tu.codeguard.config.properties.AWSProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class S3Configuration {

    private final AWSProperties awsProperties;

    /**
     * The S3AsyncClient is used for uploading/downloading data to S3 bucket
     *
     * @return S3AsyncClient
     */
    @Bean
    public S3Client s3AsyncClient() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                awsProperties.getAws().getAccessKey(),
                awsProperties.getAws().getSecretAccessKey()
        );

        SdkHttpClient customHttpClient = ApacheHttpClient.builder()
            .connectionTimeout(Duration.ofSeconds(120))
            .build();

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.EU_CENTRAL_1)
                .httpClient(customHttpClient)
                .build();
    }
}
