package com.BBHMM.backend.BBHMM.models.response;

import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.EventInvitation;
import com.BBHMM.backend.BBHMM.models.User;

import java.time.LocalDate;
import java.util.UUID;

public record EventInvitationResponse(
    UUID uuid,
    String eventName,
    String description,
    LocalDate eventDate,
    UUID invitedUserUuid,
    UserResumeResponse ownerUser
) {
    public EventInvitationResponse(UUID uuid, Event event, User invitedUser, User ownerUser) {
        this(
            uuid,
            event.getEventName(),
            event.getDescription(),
            event.getEventDate(),
            invitedUser.getUuid(),
            new UserResumeResponse(ownerUser)
        );
    }

    public EventInvitationResponse(EventInvitation eventInvitation) {
        this(
            eventInvitation.getUuid(),
            eventInvitation.getEvent().getEventName(),
            eventInvitation.getEvent().getDescription(),
            eventInvitation.getEvent().getEventDate(),
            eventInvitation.getUserInvited().getUuid(),
            new UserResumeResponse(eventInvitation.getUserOwner())
        );
    }
}
