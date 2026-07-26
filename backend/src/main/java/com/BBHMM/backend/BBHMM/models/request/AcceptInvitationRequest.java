package com.BBHMM.backend.BBHMM.models.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AcceptInvitationRequest(
    @NotNull(message = "ID é necessário")
    UUID uuid,

    @NotNull(message = "requisição necessária")
    Boolean accept
) {
}
