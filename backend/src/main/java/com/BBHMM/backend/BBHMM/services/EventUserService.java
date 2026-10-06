package com.BBHMM.backend.BBHMM.services;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.EventUser;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.UpdateEventUserRequest;
import com.BBHMM.backend.BBHMM.repositories.EventUserRepository;
import com.BBHMM.backend.BBHMM.services.validation.BillValidation;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventUserService {

    private final EventUserRepository repository;
    private final EventService eventService;
    private final BillValidation billValidation;

    @Transactional
    public EventUser updateEventUser(UpdateEventUserRequest request, UUID eventUuid, User userSecurity) {
        Event event = eventService.safeTakeEventByUuid(eventUuid);
        EventUser eventUser = event.getEventUsers()
                                    .stream()
                                    .filter(eu -> userSecurity.getUuid().equals(eu.getUserUuid()))
                                    .findFirst()
                                    .orElseThrow(() -> new InvalidDatabaseQueryException("Usuário não encontrado", "usuário não encontrado no evento", "UserID", userSecurity.getUuid().toString()));
        eventUser.update(request);

        return eventUser;
    }

    @Transactional
    public void addUser(UUID eventUuid, User guest) {
        Event event = eventService.safeTakeEventByUuid(eventUuid);
        EventUser eventUser = new EventUser(guest, event);
        event.addUser(eventUser);
    }

    public EventUser findEventUser(User user, UUID eventUuid) {
        billValidation.checkUserParticipationInEvent(user, eventUuid);
        var eventUser = repository.findByUserAndEventId(user.getUuid(), eventUuid)
            .orElseThrow(EntityNotFoundException::new);

        return eventUser;
    }

    public List<EventUser> listEventUsers(UUID eventUuid, User user) {
        billValidation.checkUserParticipationInEvent(user, eventUuid);
        var eventUsers = repository.listUsersByEvent(eventUuid);

        return eventUsers;
    }
}
