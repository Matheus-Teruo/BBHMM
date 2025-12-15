package com.BBHMM.backend.BBHMM.models.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record EmailValidaitonRequest(
    @NotBlank(message = "User ID é necessário")
    UUID userUuid
) {
}
