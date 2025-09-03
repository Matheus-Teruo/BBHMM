package com.BBHMM.backend.BBHMM.models.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record BillCreateRequest(
    @NotNull(message = "Nome de usuário é necessário")
    @Positive(message = "Valor deve ser positivo")
    BigDecimal value,
    
    @NotNull(message = "Evento é necessário")
    UUID eventUuid,

    @NotNull(message = "Pagador é necessário")
    UUID payerUuid
) {    
}
