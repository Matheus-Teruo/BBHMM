package com.BBHMM.backend.BBHMM.models.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupUserRequest(
    @NotBlank(message = "Nome de usuário é necessário")
    @Size(min = 3, message = "Nome de usuário precisa ter pelomenos 3 caractéres")
    @Pattern(regexp = "^[\\p{L}\\p{N}]*$", message = "Nome de usuário não pode ter alguns caracteres especiais")
    @Schema(example = "username")
    String username,
    
    @NotBlank(message = "Senha é necessária")
    @Size(min = 8, message = "Senha precisa ter pelomenos 8 caractéres")
    @Pattern(regexp = "^[\\w@#$%^&+=!]*$", message = "Senha não pode ter alguns caracteres especiais")
    @Schema(example = "password")
    String password,
    
    @NotBlank(message = "Nome completo é necessário")
    @Size(min = 3, message = "Seu nome completo precisa pelomenos 3 caractéres")
    @Pattern(regexp = "^[\\p{L} ]*$", message = "Seu nome completo só deve conter letras e espaço")
    @Schema(example = "user fullname")
    String fullname,

    @NotBlank(message = "Email é necessário")
    @Email(message = "Formato de e-mail inválido")
    @Schema(example = "email@example.com")
    String email,

    CreatePixRequest pix
) {
}
