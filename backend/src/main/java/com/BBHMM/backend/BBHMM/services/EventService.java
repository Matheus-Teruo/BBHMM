package com.BBHMM.backend.BBHMM.services;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.CreateEventRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateEventRequest;
import com.BBHMM.backend.BBHMM.repositories.EventRepository;
import com.BBHMM.backend.BBHMM.services.validation.EventValidation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
    
    private final EventRepository repository;
    private final EventValidation validation;
    
    @Transactional
    public Event createEvent(CreateEventRequest request, User user) {
        validation.checkNameDuplication(request.eventName());
        var event = new Event(request, user);
        repository.save(event);

        return event;
    }

    public Event getEvent(UUID uuid) {
    return repository.findByUuid(uuid)
        .orElseThrow(EntityNotFoundException::new);
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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return repository.findAllbyUser(user.getUuid());
    }

    @Transactional
    public Event updateEvent(UpdateEventRequest request) {
        validation.checkNameDuplication(request.eventName());
        var event = safeTakeEventByUuid(request.uuid());
        
        event.update(request);

        return event;
    }
}
