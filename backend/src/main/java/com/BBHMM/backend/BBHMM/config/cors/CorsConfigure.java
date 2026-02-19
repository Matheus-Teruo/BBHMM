package com.BBHMM.backend.BBHMM.config.cors;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(CorsProperties.class)
@RequiredArgsConstructor
public class CorsConfigure implements WebMvcConfigurer {

  private final CorsProperties props;

  @Override
  public void addCorsMappings(CorsRegistry registry) {

    if (props.url() == null || props.url().isBlank()) {
      return;
    }

    registry.addMapping("/**")
        .allowedOrigins(props.url().split(","))
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT")
        .allowedHeaders("Content-Type", "Accept", "Accept-Language")
        .allowCredentials(true);
  }
}

