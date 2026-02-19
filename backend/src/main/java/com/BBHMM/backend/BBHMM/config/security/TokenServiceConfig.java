package com.BBHMM.backend.BBHMM.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.BBHMM.backend.BBHMM.models.User;

@Configuration
@EnableConfigurationProperties(TokenProperties.class)
@RequiredArgsConstructor
public class TokenServiceConfig {

  private final TokenProperties props;

  public String generateToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(props.token().security());
      return JWT.create()
          .withIssuer(props.issuer())
          .withSubject(user.getUsername())
          .withClaim("id", user.getUuid().toString())
          .withExpiresAt(dataExpired())
          .sign(algorithm);
    } catch (JWTCreationException exception){
      throw new RuntimeException("error on generate token", exception);
    }
  }

  public UUID recoverUserUuid(String jwtToken) {
    try {
      return UUID.fromString(
          JWT.require(Algorithm.HMAC256(props.token().security()))
          .withIssuer(props.issuer())
          .build()
          .verify(jwtToken)
          .getClaim("id").asString());
    } catch (JWTVerificationException exception){
      throw new RuntimeException("Token JWT invalid or expired");
    }
  }

  private Instant dataExpired() {
    return LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of("-03:00"));
  }
}
