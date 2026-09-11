package com.BBHMM.backend.BBHMM.config.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Slf4j
@Configuration
@EnableConfigurationProperties(S3Properties.class)
@ConditionalOnProperty(name = "aws.s3.enabled", havingValue = "true")
@RequiredArgsConstructor
public class S3Config {

    private final S3Properties props;

    @Bean
    public S3Client s3Client() {

        S3Client client;

        if (props.endpoint() == null || props.endpoint().isBlank()) {
            client = S3Client.builder()
                    .region(Region.of(props.region()))
                    .credentialsProvider(DefaultCredentialsProvider.builder().build())
                    .build();
        } else {
            AwsBasicCredentials credentials =
                    AwsBasicCredentials.create(props.key().access(), props.key().secret());

            client = S3Client.builder()
                    .region(Region.of(props.region()))
                    .endpointOverride(URI.create(props.endpoint()))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(credentials)
                    )
                    .serviceConfiguration(
                            S3Configuration.builder()
                                    .pathStyleAccessEnabled(true)
                                    .build()
                    )
                    .build();
        }

        validateBucket(client);
        return client;
    }

    @Bean
    public S3Presigner s3Presigner() {

        S3Presigner.Builder builder = S3Presigner.builder();

        if (props.endpointPublic() == null || props.endpointPublic().isBlank()) {
            builder.credentialsProvider(
                    DefaultCredentialsProvider.builder().build()
            );
        } else {
            AwsBasicCredentials credentials =
                    AwsBasicCredentials.create(props.key().access(), props.key().secret());

            builder
                    .region(Region.of(props.region()))
                    .endpointOverride(URI.create(props.endpointPublic()))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(credentials)
                    )
                    .serviceConfiguration(
                        S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build()
                    );
        }

        return builder.build();
    }

    private void validateBucket(S3Client client) {
        try {
            client.headBucket(b -> b.bucket(props.bucket()));
            log.info("✅🪣 Connection with storage successful: bucket {}.", props.bucket());
        } catch (Exception e) {
            log.error("❌🪣 Failed to connect to storage", e);
        }
    }
}
