package com.BBHMM.backend.BBHMM.controllers;

import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.UpdateUserRequest;
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
        User user = service.updateUser(request);

        return ResponseEntity.ok(new UserResponse(user));
    }
}

