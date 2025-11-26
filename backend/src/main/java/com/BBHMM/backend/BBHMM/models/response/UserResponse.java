package com.BBHMM.backend.BBHMM.models.response;

import com.BBHMM.backend.BBHMM.models.User;

import java.util.UUID;

public record UserResponse(
    UUID uuid,
    String username,
    String role,
    String fullname,
    String email,
    PixResponse pix
) {
    public UserResponse(User user) {
        this(user.getUuid(),
            user.getUsername(),
            user.getRole().toString(),
            user.getFullname(),
            user.getEmail(),
            user.getPix() != null ? new PixResponse(user.getPix()) : null
        );
    }
}
