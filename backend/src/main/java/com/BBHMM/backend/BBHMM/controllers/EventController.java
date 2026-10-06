package com.BBHMM.backend.BBHMM.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.BBHMM.backend.BBHMM.docs.api.CreateWithReadErrors;
import com.BBHMM.backend.BBHMM.docs.api.ReadResourceErrors;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.CreateEventRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateEventRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateEventUserRequest;
import com.BBHMM.backend.BBHMM.models.response.EventResponse;
import com.BBHMM.backend.BBHMM.models.response.EventUserResponse;
import com.BBHMM.backend.BBHMM.services.EventService;
import com.BBHMM.backend.BBHMM.services.EventUserService;
import com.BBHMM.backend.BBHMM.services.StorageService;
import com.BBHMM.backend.BBHMM.services.UserService;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;
    private final UserService userService;
    private final EventUserService eventUserService;
    private final StorageService storageService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Create Event")
    @ApiResponse(responseCode = "201", description = "Event created")
    @CreateWithReadErrors
    public ResponseEntity<EventResponse> createEvent(
        @Valid @RequestBody CreateEventRequest request
    ) {
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
    @ReadResourceErrors
    public ResponseEntity<EventResponse> getEvent(
        @Valid @PathVariable UUID eventUuid
    ) {
        return ResponseEntity.ok(new EventResponse(service.getEvent(eventUuid)));
    }

    @GetMapping
    @Operation(summary = "List Event by user")
    @ApiResponse(responseCode = "200", description = "Events from user included returned")
    @ReadResourceErrors
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
    @CreateWithReadErrors
    public ResponseEntity<EventResponse> updateEvent(
        @Valid @RequestBody UpdateEventRequest request
    ) {
        var event = service.updateEvent(request);

        return ResponseEntity.ok(new EventResponse(event));
    }

    @PutMapping("/{eventUuid}/user")
    @Operation(summary = "Update User on Event")
    @ApiResponse(responseCode = "200", description = "User on event updated")
    @CreateWithReadErrors
    public ResponseEntity<EventUserResponse> updateEventUser(
        @Valid @RequestBody UpdateEventUserRequest request,
        @Valid @PathVariable UUID eventUuid
    ) {
        User userSecurity = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var eventUser = eventUserService.updateEventUser(request, eventUuid, userSecurity);
        var imageUrl = storageService.generatePresignedUrl(userSecurity.getImageKey(), Duration.ofMinutes(10));

        return ResponseEntity.ok(new EventUserResponse(userSecurity, eventUser, imageUrl));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{eventUuid}")
    @Operation(summary = "Finalize Event")
    @ApiResponse(responseCode = "204", description = "Event Finished")
    @CreateWithReadErrors
    public ResponseEntity<Void> finishEvent(
        @Valid @PathVariable UUID eventUuid
    ) {
        User userSecurity = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        service.finishEvent(eventUuid, userSecurity);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventUuid}/users")
    @Operation(summary = "List Users from Event")
    @ApiResponse(responseCode = "200", description = "Return users from event")
    @ReadResourceErrors
    public ResponseEntity<List<EventUserResponse>> listUsersFromEvent(
        @Valid @PathVariable UUID eventUuid
    ) {
        User userSecurity = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var response = eventUserService.listEventUsers(eventUuid, userSecurity)
            .stream().map(eventUser -> 
                new EventUserResponse(eventUser, storageService.generatePresignedUrl(eventUser.getUser().getImageKey(), Duration.ofMinutes(10))))
                .toList();
        return ResponseEntity.ok(response);
    }
}
