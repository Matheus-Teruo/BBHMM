package com.BBHMM.backend.BBHMM.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.BBHMM.backend.BBHMM.docs.api.CreateWithReadErrors;
import com.BBHMM.backend.BBHMM.docs.api.ReadResourceErrors;
import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.CreateGuestRequest;
import com.BBHMM.backend.BBHMM.models.request.EmailTokenRequest;
import com.BBHMM.backend.BBHMM.models.request.EmailValidaitonRequest;
import com.BBHMM.backend.BBHMM.models.request.UpgradeGuestToUserRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateUserRequest;
import com.BBHMM.backend.BBHMM.models.response.NewGuestResponse;
import com.BBHMM.backend.BBHMM.models.response.UserResponse;
import com.BBHMM.backend.BBHMM.services.EventService;
import com.BBHMM.backend.BBHMM.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final EventService eventService;

    @GetMapping("/{userUuid}")
    @Operation(summary = "Get User details")
    @ApiResponse(responseCode = "200", description = "Return user details")
    @ReadResourceErrors
    public ResponseEntity<UserResponse> getUser(@Valid @PathVariable UUID userUuid) {
        User userSecurity = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = service.getUser(userUuid, userSecurity);

        return ResponseEntity.ok(new UserResponse(user));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping
    @Operation(summary = "Update User")
    @ApiResponse(responseCode = "200", description = "User updated")
    @CreateWithReadErrors
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        User userSecurity = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = service.updateUser(request, userSecurity);

        return ResponseEntity.ok(new UserResponse(user));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/verify-email")
    @Operation(summary = "Email validation")
    @ApiResponse(responseCode = "204", description = "Email Sended")
    @CreateWithReadErrors
    public ResponseEntity<Void> verifyEmail(@Valid @RequestBody EmailValidaitonRequest request) {
        User userSecurity = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        service.verifyEmail(request ,userSecurity);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/confirm-email")
    @Operation(summary = "Confirm Email")
    @ApiResponse(responseCode = "204", description = "Email confirmed")
    @CreateWithReadErrors
    public ResponseEntity<Void> validateEmail(@Valid @RequestBody EmailTokenRequest request) {
        User userSecurity = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        service.confirmEmail(request, userSecurity);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/guest")
    @Operation(summary = "Create Guest")
    @ApiResponse(responseCode = "200", description = "Guest created")
    @CreateWithReadErrors
    public ResponseEntity<NewGuestResponse> createGuest(@Valid @RequestBody CreateGuestRequest request) {
        User hostUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        String password = service.generatePassword();
        Event event = eventService.safeTakeEventByUuid(request.eventUuid());
        User user = service.createGuest(request, hostUser, password, event);
        eventService.addUser(request.eventUuid(), user);

        return ResponseEntity.ok(new NewGuestResponse(user, password));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/guest/{guestUuid}/event/{eventUuid}")
    @Operation(summary = "Get guest data")
    @ApiResponse(responseCode = "200", description = "Return guest data and password redefined")
    @ReadResourceErrors
    public ResponseEntity<NewGuestResponse> getGuest(@Valid @PathVariable UUID guestUuid, @PathVariable UUID eventUuid) {
        User hostUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        String password = service.generatePassword();
        User user = service.getGuest(guestUuid, hostUser, eventUuid, password);

        return ResponseEntity.ok(new NewGuestResponse(user, password));
    }

    @PreAuthorize("hasRole('GUEST')")
    @PostMapping("/guest/upgrade")
    @Operation(summary = "Upgrade Guest to User")
    @ApiResponse(responseCode = "200", description = "Guest upgraded to user")
    @CreateWithReadErrors
    public ResponseEntity<UserResponse> upgradeGuestToUser(@Valid @RequestBody UpgradeGuestToUserRequest request) {
        User user = service.upgradeGuestToUser(request);

        return ResponseEntity.ok(new UserResponse(user));
    }
}

