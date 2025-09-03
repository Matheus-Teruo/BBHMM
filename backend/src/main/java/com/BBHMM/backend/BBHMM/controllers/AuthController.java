package com.BBHMM.backend.BBHMM.controllers;

import com.BBHMM.backend.BBHMM.models.request.UserCreateRequest;
import com.BBHMM.backend.BBHMM.models.request.UserLoginRequest;
import com.BBHMM.backend.BBHMM.models.response.UserResponse;
import com.BBHMM.backend.BBHMM.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService service;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody UserCreateRequest request) {
        var user = service.createUser(request);
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok("fake-jwt-token");
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }
}
