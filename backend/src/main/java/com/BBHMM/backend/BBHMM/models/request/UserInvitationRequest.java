package com.BBHMM.backend.BBHMM.models.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserInvitationRequest(
    @NotBlank(message = "Nome de usuário, nome completo ou email é necessário")
    String userfield,

    @NotNull(message = "Event ID é necessário")
    UUID eventUuid
) {
}
