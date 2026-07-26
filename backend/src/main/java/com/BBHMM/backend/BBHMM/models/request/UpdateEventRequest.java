package com.BBHMM.backend.BBHMM.models.request;

import java.time.LocalDate;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateEventRequest(
    @NotNull(message = "ID é necessário")
    UUID uuid,

    @Size(min = 3, message = "Nome do evento pelo menos 3 caracteres")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s:!\\-]*$", message = "Nome do evento não pode ter alguns caracteres especiais")
    @Schema(example = "EditedEvent")
    String eventName,

    @Size(min = 3, message = "A descrição precisa pelo menos 3 caracteres")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s/:;,.!()?%\\\\-]*$", message = "A descrição só deve conter letras, números e espaço")
    @Schema(example = "Event description edited")
    String description,

    LocalDate eventDate
) {
}
