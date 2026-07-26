package com.BBHMM.backend.BBHMM.models.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateEventRequest(
    @NotBlank(message = "Nome do evento é necessário")
    @Size(min = 3, message = "Nome do evento pelo menos 3 caracteres")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s:!\\-]*$", message = "Nome do evento não pode ter alguns caracteres especiais")
    @Schema(example = "newEvent")
    String eventName,

    @NotBlank(message = "A descrição é necessário")
    @Size(min = 3, message = "A descrição precisa pelo menos 3 caracteres")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s/:;,.!()?%\\\\-]*$", message = "A descrição só deve conter letras, números e espaço")
    @Schema(example = "Event description")
    String description,

    @NotNull(message = "A data do evento é necessário")
    LocalDate eventDate
) {
}
