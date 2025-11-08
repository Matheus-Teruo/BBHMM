package com.BBHMM.backend.BBHMM.models.response;

import java.util.UUID;

import com.BBHMM.backend.BBHMM.models.User;

public record UserListResponse(
    UUID uuid,
    String fullname,
    PixResponse pix
) {
    public UserListResponse(User user) {
        this(user.getUuid(),
            user.getFullname(),
            new PixResponse(user.getPix())
        );
    }
}
