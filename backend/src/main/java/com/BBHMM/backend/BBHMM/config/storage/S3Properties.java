package com.BBHMM.backend.BBHMM.config.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.s3")
public record S3Properties(
        String region,
        String bucket,
        String endpoint,
        String endpointPublic,
        Key key
) {
    public record Key(
            String access,
            String secret
    ) {}
}
