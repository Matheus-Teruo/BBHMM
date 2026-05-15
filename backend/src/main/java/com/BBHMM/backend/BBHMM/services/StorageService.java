package com.BBHMM.backend.BBHMM.services;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.BBHMM.backend.BBHMM.config.storage.S3Properties;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
@EnableConfigurationProperties(S3Properties.class)
@RequiredArgsConstructor
public class StorageService {

    private final S3Client s3Client;
    private final S3Presigner presigner;
    private final S3Properties props;

    public String uploadImageFile(MultipartFile file, UUID userUuid, String fileName) {
        try {
            String key = String.format(
                    "users/%s/image/%s",
                    userUuid,
                    fileName + "-" + UUID.randomUUID());

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(props.bucket())
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(
                            file.getInputStream(),
                            file.getSize()
                    )
            );

            return key;

        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload document to S3", e);
        }
    }

    public String generatePresignedUrl(String key, Duration duration) {

        PresignedGetObjectRequest presignedRequest =
                presigner.presignGetObject(p -> p
                        .getObjectRequest(r -> r
                                .bucket(props.bucket())
                                .key(key))
                        .signatureDuration(duration)
                );

        return presignedRequest.url().toString();
    }

    public void deleteFile(String key) {

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(props.bucket())
                .key(key)
                .build();

        s3Client.deleteObject(request);
    }
}
