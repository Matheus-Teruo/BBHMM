package com.BBHMM.backend.BBHMM.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.EventInvitation;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.AcceptInvitationRequest;
import com.BBHMM.backend.BBHMM.models.request.CreateEventRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateEventRequest;
import com.BBHMM.backend.BBHMM.models.request.UserInvitationRequest;
import com.BBHMM.backend.BBHMM.repositories.EventInvitationRepository;
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
    private final EventInvitationRepository eventInvitationRepository;
    private final UserService userService;
    
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

    public EventInvitation userInvitationEvent(UUID eventUuid, UserInvitationRequest request) {
        Event event = getEvent(eventUuid);
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userInvited = userService.safeTakeUserByUuid(eventUuid);
        validation.checkDuplicationInvate(request.userUuid(), eventUuid);
        validation.checkUserAlreadyInEvent(request.userUuid(), eventUuid);

        EventInvitation eventInvitation = new EventInvitation(userInvited, userOwner, event);
        eventInvitationRepository.save(eventInvitation);

        return eventInvitation;
    }

    @Transactional
    public EventInvitation acceptedInvitationEvent(UUID invitationUuid, AcceptInvitationRequest request) {
        EventInvitation eventInvitation = eventInvitationRepository.findById(invitationUuid).orElseThrow(EntityNotFoundException::new);
        User userInvitedSecurity = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userInvited = userService.safeTakeUserByUuid(userInvitedSecurity.getUuid());
        validation.checkInvateValid(eventInvitation, userInvited);

        eventInvitation.acceptInvitation(request.accept());
        if (request.accept()) eventInvitation.getEvent().addUser(userInvited);

        return eventInvitation;
    }

    public Page<EventInvitation> listEventInvitation(Pageable pageable) {
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return eventInvitationRepository.findAllByUserInvitedUuid(userOwner.getUuid(), pageable);
    }
}
