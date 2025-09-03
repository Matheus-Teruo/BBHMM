package com.BBHMM.backend.BBHMM.controllers;

import com.BBHMM.backend.BBHMM.models.request.UserUpdateRequest;
import com.BBHMM.backend.BBHMM.models.response.UserResponse;
import com.BBHMM.backend.BBHMM.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserUpdateRequest request) {
        var user = service.updateUser(request);

        return ResponseEntity.ok(new UserResponse(user));
    }
    
}

