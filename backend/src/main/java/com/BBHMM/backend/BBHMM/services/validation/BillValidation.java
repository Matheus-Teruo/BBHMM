package com.BBHMM.backend.BBHMM.services.validation;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseInsertionException;
import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BillValidation {

    private final UserRepository userRepository;

    public void checkUserParticipationInEvent(UUID userUuid, UUID eventUuid) {
        if (!userRepository.isUserParticipantInEvent(userUuid, eventUuid)) {
            throw new InvalidDatabaseQueryException(
                "Usuário inválido",
                "Usuário não pertence ao evento",
                userUuid.toString()
            );
        }
    }

    public void checkUsersParticipationInEvent(UUID eventUuid, List<UUID> participantsUuid) {
        List<User> users = userRepository.listUsersByEvent(eventUuid);
        Set<UUID> foundUserUuids = users.stream()
                                        .map(User::getUuid)
                                        .collect(Collectors.toSet());
        List<UUID> missingUsers = participantsUuid.stream()
                                                .filter(uuid -> !foundUserUuids.contains(uuid))
                                                .toList();

        if (!missingUsers.isEmpty()) {
            throw new InvalidDatabaseInsertionException(
                "Usuário(s) não encontrado(s) no evento",
                "UUID do usuário",
                Map.of(
                    "missingUserUuids",
                    missingUsers.toString(),
                    "eventUuid",
                    eventUuid.toString()
                )
            );
        }
    }
}
