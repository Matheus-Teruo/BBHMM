package com.BBHMM.backend.BBHMM.infra.exceptions;

import lombok.Getter;

@Getter
public class InvalidDatabaseQueryException extends RuntimeException {

  private final String error;
  private final String entityName;
  private final String invalidValue;

  public InvalidDatabaseQueryException(String error, String message, String entityName, String invalidValue) {
    super("Objeto com campos não encontrado: " + entityName + ": " + invalidValue + ", " + message);
    this.error = error;
    this.entityName = entityName;
    this.invalidValue = invalidValue;
  }
}