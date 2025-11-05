package com.BBHMM.backend.BBHMM.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.response.PaymentResponse;
import com.BBHMM.backend.BBHMM.services.PaymentService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService service;
    
    @GetMapping("/{eventUuid}/payment")
    public ResponseEntity<List<PaymentResponse>> getDebtPayment(@PathVariable UUID eventUuid) {
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<PaymentResponse> response = service.getPaymenList(eventUuid, userOwner);
        return ResponseEntity.ok(response);
    }
}
 