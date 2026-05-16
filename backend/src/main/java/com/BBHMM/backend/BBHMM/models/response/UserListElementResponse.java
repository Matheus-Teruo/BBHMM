package com.BBHMM.backend.BBHMM.models.response;

import java.util.UUID;

import com.BBHMM.backend.BBHMM.models.User;

public record UserListElementResponse(
    UUID uuid,
    String firstname,
    String role,
    boolean emailVerified,
    String imageUrl
) { 
    public UserListElementResponse(User user, String imageUrl) {
        this(
            user.getUuid(),
            user.getFullname().trim().split("\\s+")[0],
            user.getRole().toString(),
            user.isEmailVerified(),
            imageUrl
        );
    }
}
