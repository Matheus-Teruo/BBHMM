package com.BBHMM.backend.BBHMM.services.validation;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseInsertionException;
import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
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

    public void checkDuplicationInvate(UUID invitedUserUuid, UUID eventUuid) {
        if (eventInvitationRepository.existsByUserInvitedUuidAndEventUuidAndAcceptedIsNull(invitedUserUuid, eventUuid)) {
            throw new InvalidDatabaseInsertionException(
                "Convite pendente já existente",
                "Convite de Evento",
                Map.of(
                    "invitedUserUuid",
                    invitedUserUuid.toString(),
                    "eventUuid",
                    eventUuid.toString()
                )
            );
        }
    }

    public void checkUserAlreadyInEvent(UUID invitedUserUuid, UUID eventUuid) {
        if (repository.userAlreadyInEvent(invitedUserUuid, eventUuid)) {
            throw new InvalidDatabaseInsertionException(
                "Usuário ja está no evento",
                "Convite de Evento",
                Map.of(
                    "invitedUserUuid",
                    invitedUserUuid.toString(),
                    "eventUuid",
                    eventUuid.toString()
                )
            );
        }
    }

    public void checkInvateValid(EventInvitation eventInvitation, User userInvited) {
        if (!eventInvitation.getUserInvited().getUuid().equals(userInvited.getUuid())) {
            throw new InvalidDatabaseQueryException(
                "Concite não pertence ao mesmo usuário",
                "Convite de Evento",
                userInvited.getUuid().toString()
            );
        }
    }
}
