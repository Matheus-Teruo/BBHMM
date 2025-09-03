package com.BBHMM.backend.BBHMM.models.response;

import com.BBHMM.backend.BBHMM.models.Pix;

public record PixResponse(
    String pixKey,
    String bankAccount
) {
    public PixResponse(Pix pix){
        this(pix.getPixKey(),
            pix.getBankAccount()
        );
    }
}
