package com.BBHMM.backend.BBHMM.controllers;

import com.BBHMM.backend.BBHMM.models.Bill;
import com.BBHMM.backend.BBHMM.models.request.BillCreateRequest;
import com.BBHMM.backend.BBHMM.models.request.BillUpdateRequest;
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
    public ResponseEntity<Bill> createBill(@RequestBody BillCreateRequest request) {
        return ResponseEntity.ok(service.createBill(request));
    }

    @GetMapping("/{eventUuid}")
    public ResponseEntity<List<Bill>> listBills(@PathVariable UUID eventUuid) {
        return ResponseEntity.ok(service.listBillsByEvent(eventUuid));
    }

    @PutMapping
    public ResponseEntity<Bill> updateBill(@RequestBody BillUpdateRequest request) {
        var bill = service.updateBill(request);

        return ResponseEntity.ok(bill);
    }
}
