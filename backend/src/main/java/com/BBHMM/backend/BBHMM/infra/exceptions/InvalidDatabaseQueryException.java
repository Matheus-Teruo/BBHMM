package com.BBHMM.backend.BBHMM.infra.exceptions;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

@Getter
public class InvalidDatabaseQueryException extends RuntimeException {

  private final String error;
  private final String entityName;
  private final String invalidValue;

  public InvalidDatabaseQueryException(String error, String message, String entityName, String invalidValue) {
    super(message);
    this.error = error;
    this.entityName = entityName;
    this.invalidValue = invalidValue;
  }

  public String log(String path) {
    return Stream.of(
            "Entidade não encontrado",
            entityName != null ? "resource=" + entityName : null,
            invalidValue != null ? "fields=" + invalidValue : null,
            path != null ? "path=" + path : null,
            getMessage() != null ? "message=" + getMessage() : null
    )
    .filter(Objects::nonNull)
    .collect(Collectors.joining(" | "));
  }
}