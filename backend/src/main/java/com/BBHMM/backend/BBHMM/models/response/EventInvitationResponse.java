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
    Boolean accepted,
    UUID invitedUserUuid,
    UserResumeResponse ownerUser
) {
    public EventInvitationResponse(UUID uuid, Event event, Boolean accepted, User invitedUser, User ownerUser) {
        this(
            uuid,
            event.getEventName(),
            event.getDescription(),
            event.getEventDate(),
            accepted,
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
            eventInvitation.getAccepted(),
            eventInvitation.getUserInvited().getUuid(),
            new UserResumeResponse(eventInvitation.getUserOwner())
        );
    }
}
