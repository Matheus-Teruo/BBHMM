package com.BBHMM.backend.BBHMM.models.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record ResetGuestTokenRequest(
    @NotNull(message = "UUID do convidado é obrigatório")
    UUID guestUuid,

    @NotNull(message = "UUID do evento é obrigatório")
    UUID eventUuid
) {
}
