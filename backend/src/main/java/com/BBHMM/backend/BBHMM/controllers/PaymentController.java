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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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
    @Operation(summary = "Get total of user from event")
    @ApiResponse(responseCode = "200", description = "Return total")
    public ResponseEntity<DebitTotalResponse> getDebtTotal(@Valid @PathVariable UUID eventUuid) {
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        BigDecimal value = service.getTotal(eventUuid, userOwner);
        return ResponseEntity.ok(new DebitTotalResponse(value.compareTo(BigDecimal.ZERO) < 0,value));
    }

    @GetMapping("/{eventUuid}/payment")
    @Operation(summary = "Get list to pay")
    @ApiResponse(responseCode = "200", description = "Return list of pendent debt")
    public ResponseEntity<List<PaymentResponse>> getDebtPayment(@Valid @PathVariable UUID eventUuid) {
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<PaymentResponse> response = service.getPaymentList(eventUuid, userOwner);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventUuid}/receiving")
    @Operation(summary = "Get list to receive")
    @ApiResponse(responseCode = "200", description = "Return list of pendent receivement")
    public ResponseEntity<List<PaymentResponse>> getDebtReceiving(@Valid @PathVariable UUID eventUuid) {
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<PaymentResponse> response = service.getReceivingList(eventUuid, userOwner);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/payment")
    @Operation(summary = "Pay Bill")
    @ApiResponse(responseCode = "200", description = "Bill paid")
    public ResponseEntity<Void> payOffDebtPayment(@Valid @RequestBody PayBillRequest request) {
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        service.payOffDebit(request, userOwner);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventUuid}/payment/list")
    @Operation(summary = "List paid Bill")
    @ApiResponse(responseCode = "200", description = "Return list of paid bill")
    public ResponseEntity<List<PaymentDetailsResponse>> listOfPayrollPerUser(@Valid @PathVariable UUID eventUuid) {
        User userOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(service.listOfPayrollPerUser(eventUuid, userOwner));
    }
}