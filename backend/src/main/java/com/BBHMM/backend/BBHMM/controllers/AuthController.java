package com.BBHMM.backend.BBHMM.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.BBHMM.backend.BBHMM.config.security.TokenServiceConfig;
import com.BBHMM.backend.BBHMM.docs.api.CreateResourceErrors;
import com.BBHMM.backend.BBHMM.docs.api.CreateWithReadErrors;
import com.BBHMM.backend.BBHMM.docs.api.ReadResourceErrors;
import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.CheckResetPasswordRequest;
import com.BBHMM.backend.BBHMM.models.request.LoginUserRequest;
import com.BBHMM.backend.BBHMM.models.request.ResetPasswordRequest;
import com.BBHMM.backend.BBHMM.models.request.SignupUserRequest;
import com.BBHMM.backend.BBHMM.models.response.GuestResponse;
import com.BBHMM.backend.BBHMM.models.response.UserResponse;
import com.BBHMM.backend.BBHMM.models.response.UserResumeResponse;
import com.BBHMM.backend.BBHMM.services.EventService;
import com.BBHMM.backend.BBHMM.services.UserService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService service;
    private final EventService eventService;
    private final AuthenticationManager manager;
    private final TokenServiceConfig tokenService;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @PostMapping("/signup")
    @Operation(summary = "Create User")
    @ApiResponse(responseCode = "201", description = "User created and logged in")
    @CreateResourceErrors
    public ResponseEntity<UserResponse> signUp(
        @Valid @RequestBody
        SignupUserRequest request,
        HttpServletResponse response) {
        var user = service.createUser(request);

        var authenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        response.addCookie(createCookie(tokenJWT, 24));

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{uuid}")
            .buildAndExpand(user.getUuid())
            .toUri();
        
        return ResponseEntity.created(location).body(new UserResponse(user));
    }

    @PostMapping("/login")
    @Operation(summary = "Login User")
    @ApiResponse(responseCode = "200", description = "User logged in")
    @ReadResourceErrors
    public ResponseEntity<UserResumeResponse> login(
        @Valid @RequestBody
        LoginUserRequest request,
        HttpServletResponse response) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        response.addCookie(createCookie(tokenJWT, 24));

        return ResponseEntity.ok(new UserResumeResponse((User) authentication.getPrincipal()));
    }

    @PostMapping("/login/guest")
    @Operation(summary = "Login Guest")
    @ApiResponse(responseCode = "200", description = "Guest logged in")
    @ReadResourceErrors
    public ResponseEntity<GuestResponse> loginGuest(
        @Valid @RequestBody LoginUserRequest request,
        HttpServletResponse response) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        response.addCookie(createCookie(tokenJWT, 24));
        List<Event> events = eventService.listEvent((User) authentication.getPrincipal());

        return ResponseEntity.ok(new GuestResponse((User) authentication.getPrincipal(), events.getFirst()));
    }

    @GetMapping("/check")
    @Operation(summary = "Check User Logged Status")
    @ApiResponse(responseCode = "200", description = "User checked log status ok")
    @ReadResourceErrors
    public ResponseEntity<UserResumeResponse> user(HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(new UserResumeResponse(user));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout User")
    @ApiResponse(responseCode = "204", description = "User logged out")
    @ReadResourceErrors
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addCookie(createCookie("", 0));

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset User password")
    @ApiResponse(responseCode = "204", description = "Reset password started")
    @CreateWithReadErrors
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        service.resetPassword(request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check-reset-password")
    @Operation(summary = "Check reset User password")
    @ApiResponse(responseCode = "200", description = "User logged 15 min to change password")
    @CreateWithReadErrors
    public ResponseEntity<UserResumeResponse> checkResetPassword(
        @Valid @RequestBody CheckResetPasswordRequest request,
        HttpServletResponse response
    ) {
        String password = service.generatePassword();
        User user = service.checkResetPassword(request, password);

        var authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), password);
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        response.addCookie(createCookie(tokenJWT, 0.25f));

        return ResponseEntity.ok(new UserResumeResponse(user));
    }

    private Cookie createCookie(String tokenJWT, float hours) {
        Cookie authCookie = new Cookie("auth", tokenJWT);
        authCookie.setHttpOnly(true);
        authCookie.setPath("/");
        authCookie.setMaxAge((int) (60 * 60 * hours));
        if (activeProfile.equals("local")) {
            authCookie.setSecure(false);
            authCookie.setAttribute("SameSite", "Lax");
        } else if (activeProfile.equals("dev")) {
            authCookie.setSecure(true);
            authCookie.setAttribute("SameSite", "None");
        } else {
            authCookie.setSecure(true);
        }
        return authCookie;
    }
}
