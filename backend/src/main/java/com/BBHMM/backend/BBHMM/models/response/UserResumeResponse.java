package com.BBHMM.backend.BBHMM.models.response;

import java.util.UUID;

import com.BBHMM.backend.BBHMM.models.User;

public record UserResumeResponse(
    UUID uuid,
    String firstname
) {

    public UserResumeResponse(User user) {
        this(
            user.getUuid(),
            user.getFullname().trim().split("\\s+")[0]
        );
    }
}
