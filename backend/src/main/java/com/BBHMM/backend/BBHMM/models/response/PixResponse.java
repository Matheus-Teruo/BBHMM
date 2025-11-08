package com.BBHMM.backend.BBHMM.models.response;

import java.util.UUID;

import com.BBHMM.backend.BBHMM.models.Pix;

public record PixResponse(
    UUID uuid,
    String pixKey,
    String bankAccount
) {
    public PixResponse(Pix pix){
        this(
            pix.getUuid(),
            pix.getPixKey(),
            pix.getBankAccount()
        );
    }
}
