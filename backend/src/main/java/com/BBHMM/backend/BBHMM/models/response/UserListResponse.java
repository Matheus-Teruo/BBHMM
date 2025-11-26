package com.BBHMM.backend.BBHMM.models.response;

import java.util.UUID;

import com.BBHMM.backend.BBHMM.models.User;

public record UserListResponse(
    UUID uuid,
    String firstname,
    String fullname,
    String role,
    PixResponse pix
) {
    public UserListResponse(User user) {
        this(user.getUuid(),
            user.getFullname().trim().split("\\s+")[0],
            user.getFullname(),
            user.getRole().toString(),
            user.getPix() != null ? new PixResponse(user.getPix()) : null
        );
    }
}
