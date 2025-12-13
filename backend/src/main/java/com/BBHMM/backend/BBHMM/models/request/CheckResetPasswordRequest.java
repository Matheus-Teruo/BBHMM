package com.BBHMM.backend.BBHMM.models.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CheckResetPasswordRequest(
    @NotBlank(message = "Email é necessário")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Formato de e-mail inválido")
    String fullname,

    @NotNull(message = "Token é necessário")
    UUID token
) {
}
