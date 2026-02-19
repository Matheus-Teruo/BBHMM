package com.BBHMM.backend.BBHMM.config.cors;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "config.spring.cors.accepted")
public record CorsProperties(
    String url
) {
}
