package com.BBHMM.backend.BBHMM.models.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record PayBillRequest(
    @NotNull(message = "ID do pagador é necessário")
    UUID userToPayUuid,

    @NotNull(message = "valor é necessário")
    BigDecimal value,

    @NotNull(message = "ID do recebedor é necessário")
    UUID userToReceiveUuid,

    @NotNull(message = "ID do evento é necessário")
    UUID eventuUuid
) {
}
