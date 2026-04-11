package com.BBHMM.backend.BBHMM.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.BBHMM.backend.BBHMM.docs.api.CreateWithReadErrors;
import com.BBHMM.backend.BBHMM.docs.api.ReadResourceErrors;
import com.BBHMM.backend.BBHMM.models.Participants;
import com.BBHMM.backend.BBHMM.models.request.CreateBillRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateBillParticipantsRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateBillRequest;
import com.BBHMM.backend.BBHMM.models.response.BillResponse;
import com.BBHMM.backend.BBHMM.models.response.BillsResumeResponse;
import com.BBHMM.backend.BBHMM.services.BillService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class BillsController {

    private final BillService service;

    @PostMapping("/bills")
    @Operation(summary = "Create Bill")
    @ApiResponse(responseCode = "201", description = "Bill created")
    @CreateWithReadErrors
    public ResponseEntity<BillResponse> createBill(@Valid @RequestBody CreateBillRequest request) {
        var bill = service.createBill(request);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{uuid}")
            .buildAndExpand(bill.getUuid())
            .toUri();

            return ResponseEntity.created(location).body(new BillResponse(bill));
    }

    @GetMapping("/bills/{billUuid}")
    @Operation(summary = "Read Bill by id")
    @ApiResponse(responseCode = "200", description = "Bill returned by id")
    @ReadResourceErrors
    public ResponseEntity<BillResponse> getBills(@Valid @PathVariable UUID billUuid) {
        var bill = service.getBill(billUuid);

        return ResponseEntity.ok(new BillResponse(bill));
    }

    @GetMapping("/{eventUuid}/bills")
    @Operation(summary = "List Bills")
    @ApiResponse(responseCode = "200", description = "Bills returned by event")
    @ReadResourceErrors
    public ResponseEntity<List<BillsResumeResponse>> listBills(@Valid @PathVariable UUID eventUuid) {
        var participants = service.listParticipantsByEvent(eventUuid);
        var response = service.listBillsByEvent(eventUuid).stream().map((bill) ->
            new BillsResumeResponse(
                bill,
                participants.stream().filter(participant -> participant.getBillUuid().equals(bill.getUuid())).map(Participants::getUserUuid).toList()
            )).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/bills")
    @Operation(summary = "Update Bill")
    @ApiResponse(responseCode = "200", description = "Bill updated by id")
    @CreateWithReadErrors
    public ResponseEntity<BillResponse> updateBill(@Valid @RequestBody UpdateBillRequest request) {
        var bill = service.updateBill(request);

        return ResponseEntity.ok(new BillResponse(bill));
    }

    @PutMapping("/bills/participants")
    @Operation(summary = "Update Bill participantes")
    @ApiResponse(responseCode = "204", description = "Participants of the bill updated")
    @CreateWithReadErrors
    public ResponseEntity<Void> updateBillParticipants(@Valid @RequestBody UpdateBillParticipantsRequest request) {
        service.updateBillParticipants(request);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/bills/{billUuid}")
    @Operation(summary = "Delete Bill")
    @ApiResponse(responseCode = "204", description = "Bill deleted")
    @CreateWithReadErrors
    public ResponseEntity<Void> deleteBill(@Valid @PathVariable UUID billUuid) {
        service.deleteBill(billUuid);

        return ResponseEntity.noContent().build();
    }
}
