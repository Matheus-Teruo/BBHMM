package com.BBHMM.backend.BBHMM.controllers;

import com.BBHMM.backend.BBHMM.config.security.TokenServiceConfig;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.UserCreateRequest;
import com.BBHMM.backend.BBHMM.models.request.UserLoginRequest;
import com.BBHMM.backend.BBHMM.models.response.UserResponse;
import com.BBHMM.backend.BBHMM.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService service;

    private final AuthenticationManager manager;

    private final TokenServiceConfig tokenService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(
        @RequestBody @Valid
        UserCreateRequest request,
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
    public ResponseEntity<UserResponse> login(
        @RequestBody @Valid
        UserLoginRequest request,
        HttpServletResponse response) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        response.addCookie(createCookie(tokenJWT, 24));

        return ResponseEntity.ok(new UserResponse((User) authentication.getPrincipal()));
    }

    @GetMapping("/check")
    public ResponseEntity<UserResponse> user(HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(new UserResponse(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addCookie(createCookie("", 0));

        return ResponseEntity.noContent().build();
    }

    private Cookie createCookie(String tokenJWT, int hours) {
    Cookie authCookie = new Cookie("auth", tokenJWT);
    authCookie.setHttpOnly(true);
    authCookie.setSecure(true);
    authCookie.setPath("/");
    authCookie.setMaxAge(60 * 60 * hours);
    return authCookie;
  }
}
