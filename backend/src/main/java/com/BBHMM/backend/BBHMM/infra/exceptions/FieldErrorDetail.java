package com.BBHMM.backend.BBHMM.infra.exceptions;

public record FieldErrorDetail(
    String field,
    String message
) {
}
