package com.BBHMM.backend.BBHMM.services.validation;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseInsertionException;
import com.BBHMM.backend.BBHMM.repositories.EventRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventValidation {

    private EventRepository repository;

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
}
