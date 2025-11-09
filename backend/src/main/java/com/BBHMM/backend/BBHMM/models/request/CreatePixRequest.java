package com.BBHMM.backend.BBHMM.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreatePixRequest(
    @NotBlank(message = "Chave Pix é necessário")
    String pixKey,
    
    @NotBlank(message = "Nome do banco é necessário")
    @Pattern(regexp = "^[\\p{L}\\\\p{N} ]*$", message = "Nome do banco não pode ter alguns caracteres especiais")
    String bankAccount
) {
}
