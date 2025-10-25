package com.BBHMM.backend.BBHMM.controllers;

import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.CreateEventRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateEventRequest;
import com.BBHMM.backend.BBHMM.models.response.EventResponse;
import com.BBHMM.backend.BBHMM.models.response.UserResponse;
import com.BBHMM.backend.BBHMM.services.EventService;
import com.BBHMM.backend.BBHMM.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    private final UserService userService;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody CreateEventRequest request) {
        User userSec = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.safeTakeUserByUuid(userSec.getUuid());
        var event = service.createEvent(request, user);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{uuid}")
            .buildAndExpand(event.getUuid())
            .toUri();
   
        return ResponseEntity.created(location).body(new EventResponse(event));
    }

    @GetMapping("/{eventUuid}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable UUID eventUuid) {
        return ResponseEntity.ok(new EventResponse(service.getEvent(eventUuid)));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> listEvents() {
        var response = service.listEvent().stream().map(EventResponse::new).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<EventResponse> updateEvent(@RequestBody UpdateEventRequest request) {
        var event = service.updateEvent(request);

        return ResponseEntity.ok(new EventResponse(event));
    }

    @GetMapping("/{uuid}/users")
    public ResponseEntity<List<UserResponse>> listUsersFromEvent(@PathVariable UUID uuid) {
        var response = userService.findParticipantsByEventUuid(uuid).stream().map(UserResponse::new).toList();
        return ResponseEntity.ok(response);
    }
}
