package com.BBHMM.backend.BBHMM.controllers;

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

import com.BBHMM.backend.BBHMM.models.EventInvitation;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.AcceptInvitationRequest;
import com.BBHMM.backend.BBHMM.models.request.UserInvitationRequest;
import com.BBHMM.backend.BBHMM.models.response.EventInvitationResponse;
import com.BBHMM.backend.BBHMM.services.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventInvitationController {

    private final EventService eventService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/invitation")
    public ResponseEntity<EventInvitationResponse> userEventInvitation(
        @RequestBody UserInvitationRequest request
        ) {
        EventInvitation eventInvitation = eventService.userEventInvitation(request);
        
        return ResponseEntity.ok(new EventInvitationResponse(eventInvitation.getUuid(), eventService.getEvent(request.eventUuid()), eventInvitation.getUserInvited(), eventInvitation.getUserOwner()));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/invitation/accepted")
    public ResponseEntity<EventInvitationResponse> acceptedEventInvitation(
        @RequestBody AcceptInvitationRequest request
        ) {
        EventInvitation eventInvitation = eventService.acceptedEventInvitation(request);
        User invitedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        return ResponseEntity.ok(new EventInvitationResponse(request.uuid(), eventInvitation.getEvent(), invitedUser, eventInvitation.getUserOwner()));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/invitations")
    public ResponseEntity<Page<EventInvitationResponse>> listEventInvitation(
        @RequestParam(required = false) Boolean accepted,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        var response = eventService.listEventInvitation(accepted, pageable).map(EventInvitationResponse::new);
        
        return ResponseEntity.ok(response);
    }
}
