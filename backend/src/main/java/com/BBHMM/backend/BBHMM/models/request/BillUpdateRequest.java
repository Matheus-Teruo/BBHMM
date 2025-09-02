package com.BBHMM.backend.BBHMM.models.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BillUpdateRequest(
    @NotNull(message = "ID é necessário")
    UUID uuid,

    @Positive(message = "Valor deve ser positivo")
    BigDecimal value,

    UUID participantsUuid
) {   
}
