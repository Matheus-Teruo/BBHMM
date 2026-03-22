package com.BBHMM.backend.BBHMM.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Void> handleError404(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> invalidFields = new HashMap<>();

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      invalidFields.put(error.getField(), error.getDefaultMessage());
    }

    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("errorType", "Erro de Validação");
    response.put("invalidFields", invalidFields);
    response.put("error", "Campos inválidos");
    response.put("message", "Alguns campos contêm valores inválidos. Verifique e tente novamente.");


    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
    error.put("error",  ex.getError());
    error.put("message", ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneric(
      Exception ex,
      HttpServletRequest request
  ) {

    log.error(
        "System error | errorMessage={} path={}",
        ex.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiError.of(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Erro interno inesperado: " + ex.getMessage(),
            request.getRequestURI()
        ));
  }
}