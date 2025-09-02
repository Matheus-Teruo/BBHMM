package com.BBHMM.backend.BBHMM.controllers;

import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.EventCreateRequest;
import com.BBHMM.backend.BBHMM.models.request.EventUpdateRequest;
import com.BBHMM.backend.BBHMM.services.EventService;
import com.BBHMM.backend.BBHMM.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventCreateRequest request) {
        var event = service.createEvent(request);

        return ResponseEntity.ok(event);
    }

    @GetMapping
    public ResponseEntity<List<Event>> listEvents() {
        return ResponseEntity.ok(service.listEvent());
    }

    @PutMapping
    public ResponseEntity<Event> updateEvent(@RequestBody EventUpdateRequest request) {
        var event = service.updateEvent(request);

        return ResponseEntity.ok(event);
    }

    @GetMapping("/{uuid}/users")
    public ResponseEntity<List<User>> listUsersFromEvent(@PathVariable UUID uuid) {
        return ResponseEntity.ok(userService.findParticipantsByEventUuid(uuid));
    }
}
