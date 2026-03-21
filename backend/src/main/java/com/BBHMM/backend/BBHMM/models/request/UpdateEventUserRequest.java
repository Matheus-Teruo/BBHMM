package com.BBHMM.backend.BBHMM.models.request;

import jakarta.validation.constraints.Pattern;

public record UpdateEventUserRequest(
    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "a cor precisa ser o seguinte padrão")
    String color
) {
}
