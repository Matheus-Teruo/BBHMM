package com.BBHMM.backend.BBHMM.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
    @NotBlank(message = "Email é necessário")
    @Email(message = "Formato de e-mail inválido")
    String email
) {
}
