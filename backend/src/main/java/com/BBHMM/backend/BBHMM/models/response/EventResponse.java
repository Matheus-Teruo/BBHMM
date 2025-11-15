package com.BBHMM.backend.BBHMM.models.response;

import com.BBHMM.backend.BBHMM.models.Event;

import java.time.LocalDate;
import java.util.UUID;

public record EventResponse(
    UUID uuid,
    String eventName,
    String description,
    LocalDate eventDate
) {
    public EventResponse(Event event) {
        this(event.getUuid(),
            event.getEventName(),
            event.getDescription(),
            event.getEventDate()
        );
    }
}