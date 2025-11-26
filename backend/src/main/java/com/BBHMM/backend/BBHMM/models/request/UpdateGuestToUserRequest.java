package com.BBHMM.backend.BBHMM.models.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateGuestToUserRequest(
    @NotNull(message = "UUID do convidado é necessário")
    UUID uuid,
    
    @NotBlank(message = "O email é necessário")
    String email
) {
}
