package com.BBHMM.backend.BBHMM.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BBHMM.backend.BBHMM.models.EventInvitation;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.AcceptInvitationRequest;
import com.BBHMM.backend.BBHMM.models.request.UserInvitationRequest;
import com.BBHMM.backend.BBHMM.models.response.EventInvitationResponse;
import com.BBHMM.backend.BBHMM.services.EventService;

import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventInvitationController {

    private final EventService eventService;

    @PostMapping("/{eventUuid}/invitation")
    public ResponseEntity<EventInvitationResponse> userInvitationEvent(
        @PathVariable UUID eventUuid,
        @RequestBody UserInvitationRequest request
        ) {
        EventInvitation eventInvitation = eventService.userInvitationEvent(eventUuid, request);
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        return ResponseEntity.ok(new EventInvitationResponse(eventInvitation.getUuid(), eventService.getEvent(eventUuid), eventInvitation.getUserInvited(), userOwner));
    }

    @PostMapping("/invitation/{invitationUuid}/accepted")
    public ResponseEntity<EventInvitationResponse> acceptedInvitationEvent(
        @PathVariable UUID invitationUuid,
        @RequestBody AcceptInvitationRequest request
        ) {
        EventInvitation eventInvitation = eventService.acceptedInvitationEvent(invitationUuid, request);
        User invitedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        return ResponseEntity.ok(new EventInvitationResponse(invitationUuid, eventService.getEvent(request.eventUuid()), invitedUser, eventInvitation.getUserOwner()));
    }

    @GetMapping("/invitation")
    public ResponseEntity<Page<EventInvitationResponse>> userInvitationList(
        @PageableDefault(size = 10) Pageable pageable
    ) {
        var response = eventService.listEventInvitation(pageable).map(EventInvitationResponse::new);
        
        return ResponseEntity.ok(response);
    }
}
