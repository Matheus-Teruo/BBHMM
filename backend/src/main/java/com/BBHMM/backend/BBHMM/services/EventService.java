package com.BBHMM.backend.BBHMM.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.EventInvitation;
import com.BBHMM.backend.BBHMM.models.EventUser;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.AcceptInvitationRequest;
import com.BBHMM.backend.BBHMM.models.request.CreateEventRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateEventRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateEventUserRequest;
import com.BBHMM.backend.BBHMM.models.request.UserInvitationRequest;
import com.BBHMM.backend.BBHMM.repositories.EventInvitationRepository;
import com.BBHMM.backend.BBHMM.repositories.EventRepository;
import com.BBHMM.backend.BBHMM.services.validation.BillValidation;
import com.BBHMM.backend.BBHMM.services.validation.EventValidation;
import com.BBHMM.backend.BBHMM.services.validation.UserValidation;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {
    
    private final EventRepository repository;
    private final EventValidation validation;
    private final EventInvitationRepository eventInvitationRepository;
    private final BillValidation billValidation;
    private final UserService userService;
    private final UserValidation userValidation;
    
    @Transactional
    public Event createEvent(CreateEventRequest request, User user) {
        var event = new Event(request);

        EventUser eventUser = new EventUser(user, event);
        event.addUser(eventUser);

        return repository.save(event);
    }

    public Event getEvent(UUID uuid) {
    return repository.findByUuid(uuid)
        .orElseThrow(EntityNotFoundException::new);
    }

    public Event safeTakeEventByUuid(UUID uuid) {
    return repository.findByUuid(uuid)
        .orElseThrow(() -> new InvalidDatabaseQueryException(
            "Evento não encontrado",
            "evento inexistente",
            "ID",
            uuid.toString())
        );
    }

    public Page<Event> pageEvent(String eventName, LocalDate eventDate, Boolean finished, Pageable pageable) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return repository.findAllByUser(eventName, eventDate, finished, pageable, user.getUuid());
    }

    public List<Event> listEvent(User user) {
        return repository.findAllByUserList(user.getUuid());
    }

    @Transactional
    public Event updateEvent(UpdateEventRequest request) {
        var event = safeTakeEventByUuid(request.uuid());
        
        event.update(request);

        return event;
    }

    @Transactional
    public EventUser updateEventUser(UpdateEventUserRequest request, UUID eventUuid, User userSecurity) {
        Event event = safeTakeEventByUuid(eventUuid);
        EventUser eventUser = event.getEventUsers()
                                    .stream()
                                    .filter(eu -> userSecurity.getUuid().equals(eu.getUserUuid()))
                                    .findFirst()
                                    .orElseThrow(() -> new InvalidDatabaseQueryException("Usuário não encontrado", "usuário não encontrado no evento", "UserID", userSecurity.getUuid().toString()));
        eventUser.update(request);

        return eventUser;
    }

    public EventInvitation userEventInvitation(UserInvitationRequest request) {
        Event event = safeTakeEventByUuid(request.eventUuid());
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userInvited = userService.findUserByNameOrFullnameOrEmail(request.userField());
        validation.checkDuplicationInvite(userInvited, event);
        validation.checkUserAlreadyInEvent(userInvited, event);
        userValidation.checkIsNotAGuest(userInvited);
        billValidation.checkEventFinished(event);

        EventInvitation eventInvitation = new EventInvitation(userInvited, userOwner, event);
        eventInvitationRepository.save(eventInvitation);

        return eventInvitation;
    }

    @Transactional
    public EventInvitation acceptedEventInvitation(AcceptInvitationRequest request) {
        EventInvitation eventInvitation = eventInvitationRepository.findByUuid(request.uuid())
            .orElseThrow(EntityNotFoundException::new);
        User userInvitedSecurity = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userInvited = userService.safeTakeUserByUuid(userInvitedSecurity.getUuid());
        validation.checkInviteValid(eventInvitation, userInvited);

        eventInvitation.acceptInvitation(request.accept());
        if (request.accept()) {
            Event event = eventInvitation.getEvent();
            EventUser eventUser = new EventUser(userInvited, event);
            event.addUser(eventUser);
        }

        return eventInvitation;
    }

    public Page<EventInvitation> listEventInvitation(Boolean accepted, Pageable pageable) {
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return eventInvitationRepository.findAllByUserInvitedUuid(userOwner.getUuid(), accepted, pageable);
    }

    @Transactional
    public void addUser(UUID eventUuid, User guest) {
        Event event = safeTakeEventByUuid(eventUuid);
        EventUser eventUser = new EventUser(guest, event);
        event.addUser(eventUser);
    }

    @Transactional
    public void finishEvent(UUID eventUuid, User user) {
        Event event = safeTakeEventByUuid(eventUuid);
        validation.checkEventAvailableToFinish(event);
        billValidation.checkUserParticipationInEvent(user, eventUuid);

        event.setFinished(true);
    }
}
