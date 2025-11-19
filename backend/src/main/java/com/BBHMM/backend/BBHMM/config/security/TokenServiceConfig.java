package com.BBHMM.backend.BBHMM.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.BBHMM.backend.BBHMM.models.User;

@Configuration
public class TokenServiceConfig {

  @Value("${api.security.token.secret}")
  private String secret;

  @Value("${api.security.auth.issuer}")
  private String issuer;

  public String generateToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.create()
          .withIssuer(issuer)
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
          JWT.require(Algorithm.HMAC256(secret))
          .withIssuer(issuer)
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
