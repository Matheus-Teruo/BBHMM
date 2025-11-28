package com.BBHMM.backend.BBHMM.models.response;

import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.Event;

import java.util.UUID;

public record GuestResponse(
    UUID uuid,
    String username,
    EventResponse event
) {
    public GuestResponse(User guest, Event event) {
        this(guest.getUuid(),
            guest.getUsername(),
            new EventResponse(event)
        );
    }
}