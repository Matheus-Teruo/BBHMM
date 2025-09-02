package com.BBHMM.backend.BBHMM.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.request.EventCreateRequest;
import com.BBHMM.backend.BBHMM.models.request.EventUpdateRequest;
import com.BBHMM.backend.BBHMM.repositories.EventRepository;
import com.BBHMM.backend.BBHMM.services.validation.EventValidation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
    
    private final EventRepository repository;

    private final EventValidation validation;
    
    @Transactional
    public Event createEvent(EventCreateRequest request) {
        validation.checkNameDuplication(request.eventName());
        var event = new Event(request);
        repository.save(event);

        return event;
    }

    public Event safeTakeEventByUuid(UUID uuid) {
    return repository.findByUuid(uuid)
        .orElseThrow(() -> new InvalidDatabaseQueryException(
            "Evento não encontrado",
            "ID",
            uuid.toString())
        );
    }

    public List<Event> listEvent() {
        return repository.findAll();
    }

    @Transactional
    public Event updateEvent(EventUpdateRequest request) {
        validation.checkNameDuplication(request.eventName());
        var event = safeTakeEventByUuid(request.uuid());
        
        event.update(request);

        return event;
    }
}
