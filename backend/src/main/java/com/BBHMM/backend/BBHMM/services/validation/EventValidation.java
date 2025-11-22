package com.BBHMM.backend.BBHMM.services.validation;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseInsertionException;
import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.EventInvitation;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.repositories.EventInvitationRepository;
import com.BBHMM.backend.BBHMM.repositories.EventRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventValidation {

    private final EventRepository repository;
    private final EventInvitationRepository eventInvitationRepository;

    public void checkNameDuplication(String eventName) {
        if (eventName != null && repository.existsByEventName(eventName)) {
            throw new InvalidDatabaseInsertionException(
                "Campo duplicado",
                "Nome de usuário",
                Map.of(
                    "eventName",
                    eventName
                )
            );
        }
    }

    public void checkDuplicationInvate(User invitedUser, Event event) {
        if (eventInvitationRepository.existsByUserInvitedUuidAndEventUuidAndAcceptedIsNull(invitedUser.getUuid(), event.getUuid())) {
            throw new InvalidDatabaseInsertionException(
                "Convite pendente já existente",
                "Convite de Evento",
                Map.of(
                    "invitedUser",
                    invitedUser.getFullname().split(" ")[0],
                    "event",
                    event.getEventName()
                )
            );
        }
    }

    public void checkUserAlreadyInEvent(User invitedUser, Event event) {
        if (repository.userAlreadyInEvent(invitedUser.getUuid(), event.getUuid())) {
            throw new InvalidDatabaseInsertionException(
                "Usuário ja está no evento",
                "Convite de Evento",
                Map.of(
                    "invitedUser",
                    invitedUser.getFullname().split(" ")[0],
                    "event",
                    event.getEventName()
                )
            );
        }
    }

    public void checkInvateValid(EventInvitation eventInvitation, User userInvited) {
        if (!eventInvitation.getUserInvited().getUuid().equals(userInvited.getUuid())) {
            throw new InvalidDatabaseQueryException(
                "Concite não pertence ao mesmo usuário",
                "Convite de Evento",
                userInvited.getFullname().split(" ")[0]
            );
        }
    }
}
