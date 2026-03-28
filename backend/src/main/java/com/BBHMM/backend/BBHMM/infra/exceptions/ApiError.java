package com.BBHMM.backend.BBHMM.infra.exceptions;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;

public record ApiError(
  Instant timestamp,
  String error,
  String title,
  String message,
  String path,
  List<FieldErrorDetail> fields
) {
  public static ApiError of(
      HttpStatus status,
      String title,
      String message,
      String path
  ) {
    return new ApiError(
        Instant.now(),
        status.getReasonPhrase(),
        title,
        message,
        path,
        null
    );
  }

  public static ApiError validation(
      HttpStatus status,
      String title,
      String message,
      String path,
      List<FieldErrorDetail> fields
  ) {
    return new ApiError(
        Instant.now(),
        status.getReasonPhrase(),
        title,
        message,
        path,
        fields
    );
  }
}
