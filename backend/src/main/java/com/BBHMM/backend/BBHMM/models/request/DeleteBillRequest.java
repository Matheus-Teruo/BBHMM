package com.BBHMM.backend.BBHMM.models.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record DeleteBillRequest(
    @NotNull(message = "ID é necessário")
    UUID uuid
) {
}
