package com.BBHMM.backend.BBHMM.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;

@ConfigurationProperties(prefix = "spring.mail")
public record EmailProperties(
    @NonNull String senderEmail,
    @NonNull String redirectUrl
) {
}
