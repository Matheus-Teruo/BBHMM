package com.BBHMM.backend.BBHMM.models.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBillParticipantsRequest(
    @NotBlank(message = "Typo da requisição é necessária")
    String type,

    @NotNull(message = "ID da conta é necessário")
    UUID uuid,

    @NotNull(message = "ID do usuário é necessário")
    UUID partUuid
) {
}
