package com.BBHMM.backend.BBHMM.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiError> handleError404(
    EntityNotFoundException ex,
    HttpServletRequest request
  ) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ApiError.of(
            HttpStatus.NOT_FOUND,
            "Não encontrado",
            "Objeto não encotrado",
            request.getRequestURI()
        ));
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiError> handleAuthenticationException(
      AuthenticationException ex,
      HttpServletRequest request
  ) {
    log.warn(
        "Authentication failed | path={} | cause={}",
        request.getRequestURI(),
        ex.getMessage()
    );

    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(ApiError.of(
            HttpStatus.UNAUTHORIZED,
            "Falha na autenticação",
            "Usuário ou senha inválidos",
            request.getRequestURI()
        ));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationExceptions(
    MethodArgumentNotValidException ex,
    HttpServletRequest request
  ) {
    List<FieldErrorDetail> fields = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(err -> new FieldErrorDetail(
            err.getField(),
            err.getDefaultMessage()
        ))
        .toList();

    log.warn(
        "ValidationError | fields={} | path={} | cause={}",
        fields,
        request.getRequestURI(),
        ex.getMessage()
    );


    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ApiError.validation(
            HttpStatus.BAD_REQUEST,
            "Erro de validação",
            ex.getMessage(),
            request.getRequestURI(),
            fields
        ));
  }

  @ExceptionHandler(InvalidDatabaseInsertionException.class)
  public ResponseEntity<ApiError> handleDatabaseInsertionExceptions(
    InvalidDatabaseInsertionException ex,
    HttpServletRequest request
  ) {
    log.warn(ex.log(request.getRequestURI()));

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ApiError.validation(
            HttpStatus.BAD_REQUEST,
            ex.getError(),
            ex.getMessage(),
            request.getRequestURI(),
            ex.getFieldErrors()
        ));
  }

  @ExceptionHandler(InvalidDatabaseQueryException.class)
  public ResponseEntity<ApiError> handleDatabaseQueryExceptions(
    InvalidDatabaseQueryException ex,
    HttpServletRequest request
  ) {
    log.warn(ex.log(request.getRequestURI()));

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ApiError.of(
            HttpStatus.BAD_REQUEST,
            ex.getError(),
            ex.getMessage(),
            request.getRequestURI()
        ));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneric(
      Exception ex,
      HttpServletRequest request
  ) {
    log.error(
        "System error | type={} | errorMessage={} | path={}",
        ex.getClass().getName(),
        ex.getMessage(),
        request.getRequestURI()
    );

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiError.of(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Erro interno do sistema",
            "Erro interno inesperado: " + ex.getMessage(),
            request.getRequestURI()
        ));
  }
}