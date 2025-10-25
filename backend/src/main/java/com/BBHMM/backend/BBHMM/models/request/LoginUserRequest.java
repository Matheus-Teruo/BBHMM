package com.BBHMM.backend.BBHMM.models.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginUserRequest(
    @Size(min = 3, message = "Nome de usuário precisa ter pelomenos 3 caractéres")
    @Pattern(regexp = "^[\\p{L}\\p{N}]*$", message = "Nome de usuário não pode ter alguns caracteres especiais")
    String username,
    
    @Size(min = 8, message = "Senha precisa ter pelomenos 8 caractéres")
    @Pattern(regexp = "^[\\w@#$%^&+=!]*$", message = "Senha não pode ter alguns caracteres especiais")
    String password
) {
}
