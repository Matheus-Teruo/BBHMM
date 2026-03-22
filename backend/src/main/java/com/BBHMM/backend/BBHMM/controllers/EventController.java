package com.BBHMM.backend.BBHMM.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.CreateEventRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateEventRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateEventUserRequest;
import com.BBHMM.backend.BBHMM.models.response.EventResponse;
import com.BBHMM.backend.BBHMM.models.response.EventUserResponse;
import com.BBHMM.backend.BBHMM.services.EventService;
import com.BBHMM.backend.BBHMM.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;
    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Create Event")
    @ApiResponse(responseCode = "201", description = "Event created")
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
    @Operation(summary = "Read Event")
    @ApiResponse(responseCode = "200", description = "Event returned by id")
    public ResponseEntity<EventResponse> getEvent(@PathVariable UUID eventUuid) {
        return ResponseEntity.ok(new EventResponse(service.getEvent(eventUuid)));
    }

    @GetMapping
    @Operation(summary = "List Evvent by user")
    @ApiResponse(responseCode = "200", description = "Events from user included returned")
    public ResponseEntity<Page<EventResponse>> listEvents(
        @RequestParam(required = false) String eventName,
        @RequestParam(required = false) LocalDate eventDate,
        @RequestParam(required = false) Boolean finished,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        var response = service.pageEvent(eventName, eventDate, finished, pageable).map(EventResponse::new);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping
    @Operation(summary = "Update Event")
    @ApiResponse(responseCode = "200", description = "Bill returned by id")
    public ResponseEntity<EventResponse> updateEvent(@RequestBody UpdateEventRequest request) {
        var event = service.updateEvent(request);

        return ResponseEntity.ok(new EventResponse(event));
    }

    @PutMapping("/{eventUuid}/user")
    @Operation(summary = "Update User on Event")
    @ApiResponse(responseCode = "200", description = "User on event updated")
    public ResponseEntity<EventUserResponse> updateEventUser(
        @RequestBody UpdateEventUserRequest request,
        @PathVariable UUID eventUuid
    ) {
        User userSecurity = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var eventUser = service.updateEventUser(request, eventUuid, userSecurity);

        return ResponseEntity.ok(new EventUserResponse(userSecurity, eventUser));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{eventUuid}")
    @Operation(summary = "Finilize Event")
    @ApiResponse(responseCode = "204", description = "Event Finished")
    public ResponseEntity<Void> finishEvent(@PathVariable UUID eventUuid) {
        User userSecurity = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        service.finishEvent(eventUuid, userSecurity);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventUuid}/users")
    @Operation(summary = "List Users from Event")
    @ApiResponse(responseCode = "200", description = "Return users from event")
    public ResponseEntity<List<EventUserResponse>> listUsersFromEvent(@PathVariable UUID eventUuid) {
        var response = userService.findParticipantsByEventUuid(eventUuid).stream().map(EventUserResponse::new).toList();
        return ResponseEntity.ok(response);
    }
}
