package com.BBHMM.backend.BBHMM.controllers;

import com.BBHMM.backend.BBHMM.models.Participants;
import com.BBHMM.backend.BBHMM.models.request.CreateBillRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateBillParticipantsRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateBillRequest;
import com.BBHMM.backend.BBHMM.models.response.BillResponse;
import com.BBHMM.backend.BBHMM.models.response.BillsResumeResponse;
import com.BBHMM.backend.BBHMM.services.BillService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<BillResponse> createBill(@RequestBody CreateBillRequest request) {
        var bill = service.createBill(request);
        return ResponseEntity.ok(new BillResponse(bill));
    }

    @GetMapping("/bills/{billUuid}")
    @Operation(summary = "Read Bill by id")
    @ApiResponse(responseCode = "200", description = "Bill returned by id")
    public ResponseEntity<BillResponse> getBills(@PathVariable UUID billUuid) {
        var bill = service.getBill(billUuid);

        return ResponseEntity.ok(new BillResponse(bill));
    }

    @GetMapping("/{eventUuid}/bills")
    @Operation(summary = "List Bills")
    @ApiResponse(responseCode = "200", description = "Bills returned by event")
    public ResponseEntity<List<BillsResumeResponse>> listBills(@PathVariable UUID eventUuid) {
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
    public ResponseEntity<BillResponse> updateBill(@RequestBody UpdateBillRequest request) {
        var bill = service.updateBill(request);

        return ResponseEntity.ok(new BillResponse(bill));
    }

    @PutMapping("/bills/participants")
    @Operation(summary = "Update Bill participantes")
    @ApiResponse(responseCode = "204", description = "Participants of the bill updated")
    public ResponseEntity<Void> updateBillParticipants(@RequestBody UpdateBillParticipantsRequest request) {
        service.updateBillParticipants(request);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/bills/{billUuid}")
    @Operation(summary = "Delete Bill")
    @ApiResponse(responseCode = "204", description = "Bill deleted")
    public ResponseEntity<Void> deleteBill(@PathVariable UUID billUuid) {
        service.deleteBill(billUuid);

        return ResponseEntity.noContent().build();
    }
}
