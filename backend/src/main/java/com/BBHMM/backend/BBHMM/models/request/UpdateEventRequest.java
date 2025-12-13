package com.BBHMM.backend.BBHMM.models.request;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateEventRequest(
    @NotNull(message = "ID é necessário")
    UUID uuid,

    @Size(min = 3, message = "Nome do evento pelomenos 3 caractéres")
    @Pattern(regexp = "^[\\p{L}\\p{N}:!\\-]*$", message = "Nome do evento não pode ter alguns caracteres especiais")
    String eventName,

    @Size(min = 3, message = "A decrição precisa pelomenos 3 caractéres")
    @Pattern(regexp = "^[\\p{L}\\p{N} /:;,.!()?%\\\\-]*$", message = "A decrição só deve conter letras, numeros e espaço")
    String description,

    LocalDate eventDate
) {
}
