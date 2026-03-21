package com.BBHMM.backend.BBHMM.services.validation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseInsertionException;
import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Bill;
import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.EventUser;
import com.BBHMM.backend.BBHMM.models.Participants;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BillValidation {

    private final UserRepository userRepository;

    public void checkUserParticipationInEvent(User user, UUID eventUuid) {
        if (!userRepository.isUserParticipantInEvent(user.getUuid(), eventUuid)) {
            throw new InvalidDatabaseQueryException(
                "Usuário inválido",
                "não pertence ao evento",
                "Usuário",
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
                "não pertencem ao evento",
                "UUID do usuário",
                Map.of(
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
                "usuário tem valor á receber menor que o valor pago, causando transação desnecessária",
                "Valor recebido",
                Map.of(
                    "value",
                    value.toString()
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
                "usuário tem valor á pagar menor que o valor pago, causando transação desnecessária",
                "Valor pago",
                Map.of(
                    "value",
                    value.toString()
                )
            );
        }
    }

    public void checkEventFinished(Event event) {
        if (event.isFinished()) {
            throw new InvalidDatabaseQueryException(
                "Evento finalizado",
                "evento não pode ser modificado, pois já foi finalizado",
                "Finalizado",
                event.getEventName()
            );
        }
    }
}
