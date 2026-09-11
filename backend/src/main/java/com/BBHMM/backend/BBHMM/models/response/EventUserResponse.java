package com.BBHMM.backend.BBHMM.models.response;

import java.util.UUID;

import com.BBHMM.backend.BBHMM.models.EventUser;
import com.BBHMM.backend.BBHMM.models.User;

public record EventUserResponse(
    UUID uuid,
    String firstname,
    String fullname,
    String role,
    PixResponse pix,
    String color,
    String imageUrl
) {
    public EventUserResponse(User user, EventUser eventUser, String imageUrl) {
        this(
            user.getUuid(),
            user.getFullname().trim().split("\\s+")[0],
            user.getFullname(),
            user.getRole().toString(),
            user.getPix() != null ? new PixResponse(user.getPix()) : null,
            eventUser.getColor(),
            imageUrl
        );
    }

    public EventUserResponse(EventUser eventUser, String imageUrl) {
        this(
            eventUser.getUser().getUuid(),
            eventUser.getUser().getFullname().trim().split("\\s+")[0],
            eventUser.getUser().getFullname(),
            eventUser.getUser().getRole().toString(),
            eventUser.getUser().getPix() != null ? new PixResponse(eventUser.getUser().getPix()) : null,
            eventUser.getColor(),
            imageUrl
        );
    }
}
