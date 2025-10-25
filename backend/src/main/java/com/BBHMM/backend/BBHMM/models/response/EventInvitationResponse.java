package com.BBHMM.backend.BBHMM.models.response;

import java.util.UUID;

import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.EventInvitation;
import com.BBHMM.backend.BBHMM.models.User;

public record EventInvitationResponse(
    UUID uuid,
    String eventName,
    String description,
    UUID invitedUserUuid,
    UserResponse ownerUser
) {
    public EventInvitationResponse(UUID uuid, Event event, User invitedUser, User ownerUser) {
        this(
            uuid,
            event.getEventName(),
            event.getDescription(),
            invitedUser.getUuid(),
            new UserResponse(ownerUser)
        );
    }

    public EventInvitationResponse(EventInvitation eventInvitation) {
        this(
            eventInvitation.getUuid(),
            eventInvitation.getEvent().getEventName(),
            eventInvitation.getEvent().getDescription(),
            eventInvitation.getUserInvited().getUuid(),
            new UserResponse(eventInvitation.getUserOwner())
        );
    }
}
