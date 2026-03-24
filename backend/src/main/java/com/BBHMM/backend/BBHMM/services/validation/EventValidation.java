package com.BBHMM.backend.BBHMM.services.validation;

import org.springframework.stereotype.Component;

import com.BBHMM.backend.BBHMM.infra.exceptions.FieldErrorDetail;
import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseInsertionException;
import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.EventInvitation;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.repositories.BillRepository;
import com.BBHMM.backend.BBHMM.repositories.EventInvitationRepository;
import com.BBHMM.backend.BBHMM.repositories.EventRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventValidation {

    private final EventRepository repository;
    private final EventInvitationRepository eventInvitationRepository;
    private final BillRepository billRepository;

    public void checkDuplicationInvate(User invitedUser, Event event) {
        if (eventInvitationRepository.existsByUserInvitedUuidAndEventUuidAndAcceptedIsNull(invitedUser.getUuid(), event.getUuid())) {
            throw new InvalidDatabaseInsertionException(
                "Convite pendente já existente",
                "Usuário ja possui um convite pendente para esse evento",
                "EventInvitation",
                List.of(
                    new FieldErrorDetail(
                        "invitedUser",
                        invitedUser.getFullname().split(" ")[0]),
                    new FieldErrorDetail(
                        "event",
                        event.getEventName()
                    )
                )
            );
        }
    }

    public void checkUserAlreadyInEvent(User invitedUser, Event event) {
        if (repository.userAlreadyInEvent(invitedUser.getUuid(), event.getUuid())) {
            throw new InvalidDatabaseInsertionException(
                "Usuário ja está no evento",
                "Usuário já participa desse evento, não é possivel gerar convite",
                "EventInvitation",
                List.of(
                    new FieldErrorDetail(
                        "invitedUser",
                        invitedUser.getFullname().split(" ")[0]),
                    new FieldErrorDetail(
                        "event",
                        event.getEventName())
                )
            );
        }
    }

    public void checkInvateValid(EventInvitation eventInvitation, User userInvited) {
        if (!eventInvitation.getUserInvited().getUuid().equals(userInvited.getUuid())) {
            throw new InvalidDatabaseQueryException(
                "Convite não pode ser aceito",
                "Convite não pertence ao usuário",
                "EventInvitation",
                userInvited.getFullname().split(" ")[0]
            );
        }
    }

    public void checkEventAvaliableToFinish(Event event) {
        if (!billRepository.findBillsByEventUuidAndNotPaid(event.getUuid()).isEmpty()) {
            throw new InvalidDatabaseQueryException(
                "Evento não pode ser finalizado",
                "Evento com conta ativa não pode ser finalizado",
                "Event",
                event.getEventName()
            );
        }
    }
}
