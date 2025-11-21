package com.BBHMM.backend.BBHMM.controllers;

import com.BBHMM.backend.BBHMM.models.Participants;
import com.BBHMM.backend.BBHMM.models.request.CreateBillRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateBillParticipantsRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateBillRequest;
import com.BBHMM.backend.BBHMM.models.response.BillResponse;
import com.BBHMM.backend.BBHMM.models.response.BillsResumeResponse;
import com.BBHMM.backend.BBHMM.services.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class BillsController {

    private final BillService service;

    @PostMapping("/bills")
    public ResponseEntity<BillResponse> createBill(@RequestBody CreateBillRequest request) {
        var bill = service.createBill(request);
        return ResponseEntity.ok(new BillResponse(bill));
    }

    @GetMapping("/bills/{billUuid}")
    public ResponseEntity<BillResponse> getBills(@PathVariable UUID billUuid) {
        var bill = service.getBill(billUuid);

        return ResponseEntity.ok(new BillResponse(bill));
    }

    @GetMapping("/{eventUuid}/bills")
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
    public ResponseEntity<BillResponse> updateBill(@RequestBody UpdateBillRequest request) {
        var bill = service.updateBill(request);

        return ResponseEntity.ok(new BillResponse(bill));
    }

    @PutMapping("/bills/participants")
    public ResponseEntity<Void> updateBillParticipants(@RequestBody UpdateBillParticipantsRequest request) {
        service.updateBillParticipants(request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bills/{billUuid}")
    public ResponseEntity<Void> deleteBill(@PathVariable UUID billUuid) {
        service.deleteBill(billUuid);

        return ResponseEntity.noContent().build();
    }
}
