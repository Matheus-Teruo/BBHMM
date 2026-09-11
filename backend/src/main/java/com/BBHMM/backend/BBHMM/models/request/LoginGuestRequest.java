package com.BBHMM.backend.BBHMM.models.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginGuestRequest(
    @Size(min = 3, message = "Nome de usuário precisa ter pelo menos 3 caracteres")
    @Pattern(regexp = "^[\\p{L}\\p{N}-]*$", message = "Nome de usuário não pode ter alguns caracteres especiais")
    @Schema(example = "username-test")
    String username,
    
    @Size(min = 8, message = "Senha precisa ter pelo menos 8 caracteres")
    @Pattern(regexp = "^[\\w@#$%^&+=!]*$", message = "Senha não pode ter alguns caracteres especiais")
    @Schema(example = "password")
    String password
) {
}
