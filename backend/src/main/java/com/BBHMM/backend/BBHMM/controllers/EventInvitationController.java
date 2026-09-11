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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BBHMM.backend.BBHMM.docs.api.CreateWithReadErrors;
import com.BBHMM.backend.BBHMM.docs.api.ReadResourceErrors;
import com.BBHMM.backend.BBHMM.models.EventInvitation;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.AcceptInvitationRequest;
import com.BBHMM.backend.BBHMM.models.request.UserInvitationRequest;
import com.BBHMM.backend.BBHMM.models.response.EventInvitationResponse;
import com.BBHMM.backend.BBHMM.services.EventService;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventInvitationController {

    private final EventService eventService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/invitation")
    @Operation(summary = "Invite User to event")
    @ApiResponse(responseCode = "200", description = "User invited to event")
    @ReadResourceErrors
    public ResponseEntity<EventInvitationResponse> userEventInvitation(
        @Valid @RequestBody UserInvitationRequest request
        ) {
        EventInvitation eventInvitation = eventService.userEventInvitation(request);
        
        return ResponseEntity.ok(new EventInvitationResponse(eventInvitation.getUuid(), eventService.getEvent(request.eventUuid()), eventInvitation.getAccepted(), eventInvitation.getUserInvited(), eventInvitation.getUserOwner()));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/invitation/accepted")
    @Operation(summary = "Accept Invite")
    @ApiResponse(responseCode = "200", description = "Invite accepted and user is part of event")
    @CreateWithReadErrors
    public ResponseEntity<EventInvitationResponse> acceptedEventInvitation(
        @Valid @RequestBody AcceptInvitationRequest request
        ) {
        EventInvitation eventInvitation = eventService.acceptedEventInvitation(request);
        User invitedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        return ResponseEntity.ok(new EventInvitationResponse(request.uuid(), eventInvitation.getEvent(), eventInvitation.getAccepted(), invitedUser, eventInvitation.getUserOwner()));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/invitations")
    @Operation(summary = "List invitations")
    @ApiResponse(responseCode = "200", description = "Return invitation of user")
    @ReadResourceErrors
    public ResponseEntity<Page<EventInvitationResponse>> listEventInvitation(
        @RequestParam(required = false) Boolean accepted,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        var response = eventService.listEventInvitation(accepted, pageable).map(EventInvitationResponse::new);
        
        return ResponseEntity.ok(response);
    }
}
