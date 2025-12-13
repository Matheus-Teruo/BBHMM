package com.BBHMM.backend.BBHMM.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateBillRequest(
    @NotBlank(message = "Nome da conta deve existir")   
    @Size(min = 3, message = "Nome da conta pelomenos 3 caractéres")
    @Pattern(regexp = "^[\\p{L}\\p{N}:!\\-]*$", message = "Nome da conta não pode ter alguns caracteres especiais")
    String name,
    
    @Size(min = 3, message = "A decrição precisa pelomenos 3 caractéres")
    @Pattern(regexp = "^[\\p{L}\\p{N} /:;,.!()?%\\\\-]*$", message = "A decrição só deve conter letras, numeros e espaço")
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
