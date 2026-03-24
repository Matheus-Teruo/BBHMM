package com.BBHMM.backend.BBHMM.infra.exceptions;

import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class InvalidDatabaseInsertionException extends RuntimeException {

  private final String error;
  private final String entityName;
  private final List<FieldErrorDetail> fieldErrors;

  public InvalidDatabaseInsertionException(String error, String message, String entityName, List<FieldErrorDetail> fieldErrors) {
    super(message);
    this.error = error;
    this.entityName = entityName;
    this.fieldErrors = fieldErrors;
  }

  public String log(String path) {
    return Stream.of(
            "Falha ao Criar ou Atualizar entidade",
            entityName != null ? "resource=" + entityName : null,
            formatFields() != null ? "fields=" + formatFields() : null,
            path != null ? "path=" + path : null,
            getMessage() != null ? "message=" + getMessage() : null
    )
    .filter(Objects::nonNull)
    .collect(Collectors.joining(" | "));
  }

  private String formatFields() {
    if (fieldErrors == null || fieldErrors.isEmpty()) return null;

    return fieldErrors.stream()
            .map(e -> e.field() + ":" + e.message())
            .collect(Collectors.joining(", "));
}
}
