package com.BBHMM.backend.BBHMM.models.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record UpdateEventUserRequest(
    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "a cor precisa ser o seguinte padrão")
    @Schema(example = "#9B63DB")
    String color
) {
}
