package com.BBHMM.backend.BBHMM.models.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record UserInvitationRequest(
    @NotNull(message = "User ID é necessário")
    UUID userUuid,

    @NotNull(message = "Event ID é necessário")
    UUID eventUuid
) {
}
