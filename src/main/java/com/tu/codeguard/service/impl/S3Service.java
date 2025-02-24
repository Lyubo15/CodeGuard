//package com.tu.codeguard.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import software.amazon.awssdk.core.ResponseInputStream;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//import software.amazon.awssdk.services.s3.model.GetObjectResponse;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//
//import java.util.zip.ZipInputStream;
//
//import static java.nio.charset.StandardCharsets.UTF_8;
//
//@Component
//@Slf4j
//public class S3Service {
//    private final S3Client s3Client;
//
//    public S3Service(S3Client s3Client) {
//        this.s3Client = s3Client;
//    }
//
//    public void upload(String key, String bucket, String content) {
//        PutObjectRequest putRequest = PutObjectRequest.builder()
//                .bucket(bucket)
//                .key(key)
//                .contentType("text/plain")
//                .build();
//
//        RequestBody asyncRequestBody = RequestBody.fromBytes(content.getBytes(UTF_8));
//        s3Client.putObject(putRequest, asyncRequestBody);
//
//        log.info("Upload to S3. Key: {}", key);
//    }
//
//    public ZipInputStream download(String key, String bucket) {
//        GetObjectRequest objectRequest = GetObjectRequest.builder()
//            .key(key)
//            .bucket(bucket)
//            .build();
//
//        ResponseInputStream<GetObjectResponse> objectBytes = s3Client.getObject(objectRequest);
//
//        log.info("Download from S3. Key: {}", key);
//        return new ZipInputStream(objectBytes);
//    }
//}
