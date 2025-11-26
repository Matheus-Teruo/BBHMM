package com.BBHMM.backend.BBHMM.controllers;

import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.CreateGuestRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateGuestToUserRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateUserRequest;
import com.BBHMM.backend.BBHMM.models.response.NewGuestResponse;
import com.BBHMM.backend.BBHMM.models.response.UserResponse;
import com.BBHMM.backend.BBHMM.services.UserService;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/{userUuid}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userUuid) {
        User userSecurity = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = service.getUser(userUuid, userSecurity);

        return ResponseEntity.ok(new UserResponse(user));
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody UpdateUserRequest request) {
        User userSecurity = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = service.updateUser(request, userSecurity);

        return ResponseEntity.ok(new UserResponse(user));
    }

    @PostMapping
    public ResponseEntity<NewGuestResponse> createGuest(@RequestBody CreateGuestRequest request) {
        User hostUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String password = service.generatePassword();
        User user = service.createGuest(request, hostUser, password);

        return ResponseEntity.ok(new NewGuestResponse(user, password));
    }

    @PostMapping
    public ResponseEntity<UserResponse> updateGuestToUser(@RequestBody UpdateGuestToUserRequest request) {
        User user = service.updateGuestToUser(request);

        return ResponseEntity.ok(new UserResponse(user));
    }
}

