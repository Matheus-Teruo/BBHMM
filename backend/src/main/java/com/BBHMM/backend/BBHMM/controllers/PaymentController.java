package com.BBHMM.backend.BBHMM.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.PayBillRequest;
import com.BBHMM.backend.BBHMM.models.response.DebitTotalResponse;
import com.BBHMM.backend.BBHMM.models.response.PaymentDetailsResponse;
import com.BBHMM.backend.BBHMM.models.response.PaymentResponse;
import com.BBHMM.backend.BBHMM.services.PaymentService;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @GetMapping("/{eventUuid}/total")
    public ResponseEntity<DebitTotalResponse> getDebtTotal(@PathVariable UUID eventUuid) {
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        BigDecimal value = service.getTotal(eventUuid, userOwner);
        return ResponseEntity.ok(new DebitTotalResponse(value.compareTo(BigDecimal.ZERO) < 0,value));
    }

    @GetMapping("/{eventUuid}/payment")
    public ResponseEntity<List<PaymentResponse>> getDebtPayment(@PathVariable UUID eventUuid) {
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<PaymentResponse> response = service.getPaymentList(eventUuid, userOwner);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventUuid}/receiving")
    public ResponseEntity<List<PaymentResponse>> getDebtReceiving(@PathVariable UUID eventUuid) {
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<PaymentResponse> response = service.getReceivingList(eventUuid, userOwner);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/payment")
    public ResponseEntity<Void> payOffDebtPayment(@RequestBody PayBillRequest request) {
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        service.payOffDebit(request, userOwner);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventUuid}/payment/list")
    public ResponseEntity<List<PaymentDetailsResponse>> listOfPayrollPerUser(@PathVariable UUID eventUuid) {
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(service.listOfPayrollPerUser(eventUuid, userOwner));
    }
}