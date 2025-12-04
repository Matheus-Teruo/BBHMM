package com.BBHMM.backend.BBHMM.models.response;

import java.util.UUID;

import com.BBHMM.backend.BBHMM.models.User;

public record NewGuestResponse(
    UUID uuid,
    String username,
    String fullname,
    String token
) {
    public NewGuestResponse(User user, String password) {
        this(
            user.getUuid(),
            user.getUsername(),
            user.getFullname(),
            password
        );
    }
}
