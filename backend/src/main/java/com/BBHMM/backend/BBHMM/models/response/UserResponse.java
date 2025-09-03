package com.BBHMM.backend.BBHMM.models.response;

import com.BBHMM.backend.BBHMM.models.User;

import java.util.UUID;

public record UserResponse(
    UUID uuid,
    String username,
    String fullname,
    PixResponse Pix
) {
    public UserResponse(User user) {
        this(user.getUuid(),
            user.getUsername(),
            user.getFullname(),
            new PixResponse(user.getPix())
        );
    }
}
