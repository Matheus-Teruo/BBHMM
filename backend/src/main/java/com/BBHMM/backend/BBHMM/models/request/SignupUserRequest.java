package com.BBHMM.backend.BBHMM.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupUserRequest(
    @NotBlank(message = "Nome de usuário é necessário")
    @Size(min = 3, message = "Nome de usuário precisa ter pelomenos 3 caractéres")
    @Pattern(regexp = "^[\\p{L}\\p{N}]*$", message = "Nome de usuário não pode ter alguns caracteres especiais")
    String username,
    
    @NotBlank(message = "Senha é necessária")
    @Size(min = 8, message = "Senha precisa ter pelomenos 8 caractéres")
    @Pattern(regexp = "^[\\w@#$%^&+=!]*$", message = "Senha não pode ter alguns caracteres especiais")
    String password,
    
    @NotBlank(message = "Nome completo é necessário")
    @Size(min = 3, message = "Seu nome completo precisa pelomenos 3 caractéres")
    @Pattern(regexp = "^[\\p{L} ]*$", message = "Seu nome completo só deve conter letras e espaço")
    String fullname,

    @NotBlank(message = "Email é necessário")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Formato de e-mail inválido")
    String email,

    CreatePixRequest pix
) {
}
