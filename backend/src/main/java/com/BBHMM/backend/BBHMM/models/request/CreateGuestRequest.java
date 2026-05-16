package com.BBHMM.backend.BBHMM.models.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateGuestRequest(
    @NotBlank(message = "O nome do convidado é necessário")
    @Pattern(regexp = "^[\\p{L} ]*$", message = "O nome do convidado deve conter apenas letras e espaço")
    String guestName,

    @NotNull(message = "O evento é necessário")
    UUID eventUuid
) {
}
