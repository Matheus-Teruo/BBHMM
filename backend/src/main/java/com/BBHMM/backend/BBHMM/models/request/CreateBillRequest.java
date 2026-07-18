package com.BBHMM.backend.BBHMM.models.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateBillRequest(
    @NotBlank(message = "Nome da conta deve existir")   
    @Size(min = 3, message = "Nome da conta pelo menos 3 caracteres")
    @Pattern(regexp = "^[\\p{L}\\p{N}:!\\-]*$", message = "Nome da conta não pode ter alguns caracteres especiais")
    @Schema(example = "newBill")
    String name,

    @Size(min = 3, message = "A descrição precisa pelo menos 3 caracteres")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s/:;,.!()?%\\\\-]*$", message = "A descrição só deve conter letras, números e espaço")
    @Schema(example = "Bill description")
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
