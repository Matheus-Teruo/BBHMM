package com.BBHMM.backend.BBHMM.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Void> handleError404(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @ExceptionHandler(InvalidDatabaseInsertionException.class)
  public ResponseEntity<Map<String, Object>> handleDatabaseInsertionExceptions(InvalidDatabaseInsertionException ex) {
    Map<String, Object> error = new HashMap<>();
    error.put("errorType",
        "Erro ao Inserir no Banco"
    );
    error.put("entity", ex.getEntityName());
    error.put("invalidFields", ex.getFieldErrors());
    error.put("error", ex.getError());
    error.put("message", ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(InvalidDatabaseQueryException.class)
  public ResponseEntity<Map<String, String>> handleDatabaseQueryExceptions(InvalidDatabaseQueryException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("errorType","Componentem não existente");
    error.put("entity", ex.getEntityName());
    error.put("invalidValue", ex.getInvalidValue());
    error.put("error", ex.getMessage());
    error.put("message", ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }
}