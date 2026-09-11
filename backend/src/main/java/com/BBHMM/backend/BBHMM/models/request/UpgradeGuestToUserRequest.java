package com.BBHMM.backend.BBHMM.models.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpgradeGuestToUserRequest(
    @NotNull(message = "UUID do convidado é necessário")
    UUID uuid,

    @NotBlank(message = "Senha é necessária")
    @Size(min = 8, message = "Senha precisa ter pelo menos 8 caracteres")
    @Pattern(regexp = "^[\\w@#$%^&+=!]*$", message = "Senha não pode ter alguns caracteres especiais")
    String password,

    @NotBlank(message = "O email é necessário")
    String email
) {
}
