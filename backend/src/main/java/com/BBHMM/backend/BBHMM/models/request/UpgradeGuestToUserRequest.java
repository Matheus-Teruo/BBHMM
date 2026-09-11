package com.BBHMM.backend.BBHMM.models.request;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpgradeGuestToUserRequest(
    @NotNull(message = "UUID do convidado é necessário")
    UUID uuid,

    @NotBlank(message = "Nome de usuário é necessário")
    @Size(min = 3, message = "Nome de usuário precisa ter pelo menos 3 caracteres")
    @Pattern(regexp = "^[\\p{L}\\p{N}]*$", message = "Nome de usuário não pode caracteres especiais")
    @Schema(example = "username")
    String username,

    @NotBlank(message = "Senha é necessária")
    @Size(min = 8, message = "Senha precisa ter pelo menos 8 caracteres")
    @Pattern(regexp = "^[\\w@#$%^&+=!]*$", message = "Senha não pode ter alguns caracteres especiais")
    @Schema(example = "password")
    String password,

    @NotBlank(message = "O email é necessário")
    @Schema(example = "username")
    String email
) {
}
