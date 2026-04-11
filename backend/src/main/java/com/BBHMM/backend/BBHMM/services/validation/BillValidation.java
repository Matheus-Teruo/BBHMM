package com.BBHMM.backend.BBHMM.services.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.BBHMM.backend.BBHMM.infra.exceptions.FieldErrorDetail;
import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseInsertionException;
import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Bill;
import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.EventUser;
import com.BBHMM.backend.BBHMM.models.Participants;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BillValidation {

    private final UserRepository userRepository;

    public void checkUserParticipationInEvent(User user, UUID eventUuid) {
        if (!userRepository.isUserParticipantInEvent(user.getUuid(), eventUuid)) {
            throw new InvalidDatabaseQueryException(
                "Usuário inválido",
                "Usuário Não pertence ao evento",
                "User",
                user.getFullname().split(" ")[0]
            );
        }
    }

    public void checkUsersParticipationInEvent(UUID eventUuid, List<UUID> participantsUuid) {
        List<EventUser> eventUsers = userRepository.listUsersByEvent(eventUuid);
        Set<UUID> foundUserUuids = eventUsers.stream()
                                        .map(EventUser::getUserUuid)
                                        .collect(Collectors.toSet());
        List<UUID> missingUsers = participantsUuid.stream()
                                                .filter(uuid -> !foundUserUuids.contains(uuid))
                                                .toList();

        if (!missingUsers.isEmpty()) {
            throw new InvalidDatabaseInsertionException(
                "Usuário(s) não encontrado(s) no evento",
                "Usuários não pertencem ao evento",
                "EventUser",
                List.of(
                )
            );
        }
    }

    public void checkValueOfPaymentMatches(List<Bill> receiverBills, BigDecimal value, List<Participants> payerParticipants) {
        BigDecimal totalToReceive = receiverBills.stream()
            .map(Bill::getRemainingBalance)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalToReceive.compareTo(value) < 0) {
            throw new InvalidDatabaseInsertionException(
                "Valor passa o que deve receber",
                "Usuário precisa receber valor menor que o valor pago, causando transação desnecessária",
                "Bill",
                List.of(new FieldErrorDetail(
                    "value",
                    value.toString())
                )
            );
        }

        BigDecimal totalToPay = payerParticipants.stream()
            .map(Participants::getRemainingBalance)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalToPay.compareTo(value) < 0) {
            throw new InvalidDatabaseInsertionException(
                "Valor passa o que deve pagar",
                "Usuário tem valor á pagar menor que o valor pago, causando transação desnecessária",
                "Bill",
                List.of(new FieldErrorDetail(
                    "value",
                    value.toString())
                )
            );
        }
    }

    public void checkEventFinished(Event event) {
        if (event.isFinished()) {
            throw new InvalidDatabaseQueryException(
                "Evento finalizado",
                "Evento não pode ser modificado, pois já foi finalizado",
                "Event",
                event.getEventName()
            );
        }
    }
}
