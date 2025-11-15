package com.BBHMM.backend.BBHMM.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateEventRequest(
    @NotBlank(message = "Nome do evento é necessário")
    @Size(min = 3, message = "Nome do evento pelomenos 3 caractéres")
    @Pattern(regexp = "^[\\p{L}\\p{N}]*$", message = "Nome do evento não pode ter alguns caracteres especiais")
    String eventName,

    
    @NotBlank(message = "A decrição é necessário")
    @Size(min = 3, message = "A decrição precisa pelomenos 3 caractéres")
    @Pattern(regexp = "^[\\p{L}\\p{N} ]*$", message = "A decrição só deve conter letras, numeros e espaço")
    String description,

    @NotNull(message = "A data do evento é necessário")
    LocalDate eventDate
) {
}
