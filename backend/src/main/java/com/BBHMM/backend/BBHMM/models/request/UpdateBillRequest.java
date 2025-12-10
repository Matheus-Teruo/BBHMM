package com.BBHMM.backend.BBHMM.models.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateBillRequest(

    @NotNull(message = "ID é necessário")
    UUID uuid,
    
    @Size(min = 3, message = "Nome da conta pelomenos 3 caractéres")
    @Pattern(regexp = "^[\\p{L}\\p{N}:!\\-]*$", message = "Nome da conta não pode ter alguns caracteres especiais")
    String name,
    
    @Size(min = 3, message = "A decrição precisa pelomenos 3 caractéres")
    @Pattern(regexp = "^[\\p{L}\\p{N} /:;,.!()?%\\\\-]*$", message = "A decrição só deve conter letras, numeros e espaço")
    String description,

    @Positive(message = "Valor deve ser positivo")
    BigDecimal value,

    List<UUID> listPartUuids
) {   
}
