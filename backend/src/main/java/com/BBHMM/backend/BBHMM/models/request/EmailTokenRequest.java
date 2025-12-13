package com.BBHMM.backend.BBHMM.models.request;

import jakarta.validation.constraints.NotBlank;

public record EmailTokenRequest(
    @NotBlank(message = "Token é necessário")
    String email
) {
}
