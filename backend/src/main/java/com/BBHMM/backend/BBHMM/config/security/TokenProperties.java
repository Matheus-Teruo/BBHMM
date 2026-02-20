package com.BBHMM.backend.BBHMM.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "config.spring.security")
public record TokenProperties(
    String issuer,
    TokenSecurity token
) {
    public record TokenSecurity (
        String secret
    ) {
    }
}
