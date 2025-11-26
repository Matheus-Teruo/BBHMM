package com.BBHMM.backend.BBHMM.models.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateGuestRequest(
    @NotBlank(message = "O nome do convidado é necessário")
    String guestName,

    @NotNull(message = "O evento é necessário")
    UUID eventUuid
) {
}
