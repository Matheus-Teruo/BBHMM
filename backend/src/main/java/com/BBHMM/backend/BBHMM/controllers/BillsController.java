package com.BBHMM.backend.BBHMM.controllers;

import com.BBHMM.backend.BBHMM.models.request.BillCreateRequest;
import com.BBHMM.backend.BBHMM.models.request.BillUpdateRequest;
import com.BBHMM.backend.BBHMM.models.response.BillResponse;
import com.BBHMM.backend.BBHMM.services.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/event/bills")
@RequiredArgsConstructor
public class BillsController {

    private final BillService service;

    @PostMapping
    public ResponseEntity<BillResponse> createBill(@RequestBody BillCreateRequest request) {
        var bill = service.createBill(request);
        return ResponseEntity.ok(new BillResponse(bill));
    }

    @GetMapping("/{eventUuid}")
    public ResponseEntity<List<BillResponse>> listBills(@PathVariable UUID eventUuid) {
        var response = service.listBillsByEvent(eventUuid).stream().map(BillResponse::new).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<BillResponse> updateBill(@RequestBody BillUpdateRequest request) {
        var bill = service.updateBill(request);

        return ResponseEntity.ok(new BillResponse(bill));
    }
}
