package com.BBHMM.backend.BBHMM.services.validation;

import org.springframework.stereotype.Component;
import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseInsertionException;
import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.EventInvitation;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.repositories.EventInvitationRepository;
import com.BBHMM.backend.BBHMM.repositories.EventRepository;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EventValidation {

    private final EventRepository repository;
    private final EventInvitationRepository eventInvitationRepository;

    public void checkDuplicationInvate(User invitedUser, Event event) {
        if (eventInvitationRepository.existsByUserInvitedUuidAndEventUuidAndAcceptedIsNull(invitedUser.getUuid(), event.getUuid())) {
            throw new InvalidDatabaseInsertionException(
                "Convite pendente já existente",
                "usuário ja possui um convite pendente para esse evento",
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
                "usuário já participa desse evento",
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
                "Convite não pode ser aceito",
                "convite não pertence ao usuário",
                "Convite de Evento",
                userInvited.getFullname().split(" ")[0]
            );
        }
    }
}
