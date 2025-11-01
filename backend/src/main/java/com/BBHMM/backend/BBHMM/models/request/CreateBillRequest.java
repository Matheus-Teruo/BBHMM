package com.BBHMM.backend.BBHMM.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record CreateBillRequest(

    @NotBlank(message = "Nome da conta deve existir")
    String name,
    
    String description,

    @NotNull(message = "Valor é necessário")
    @Positive(message = "Valor deve ser positivo")
    BigDecimal value,
    
    @NotNull(message = "Evento é necessário")
    UUID eventUuid,

    @NotNull(message = "Pagador é necessário")
    UUID payerUuid
) {    
}
