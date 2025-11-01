package com.BBHMM.backend.BBHMM.models.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateBillRequest(

    @NotNull(message = "ID é necessário")
    UUID uuid,
    
    String name,
    
    String description,

    @Positive(message = "Valor deve ser positivo")
    BigDecimal value,

    List<UUID> listPartUuids
) {   
}
