package com.BBHMM.backend.BBHMM.infra.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;

public record ApiError(
  Instant timestamp,
  String error,
  String message,
  String path
) {
  public static ApiError of(
      HttpStatus status,
      String message,
      String path
  ) {
    return new ApiError(
        Instant.now(),
        status.getReasonPhrase(),
        message,
        path
    );
  }
}
