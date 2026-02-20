package com.BBHMM.backend.BBHMM.models.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record CheckResetPasswordRequest(
    @NotNull(message = "Token é necessário")
    UUID token
) {
}
