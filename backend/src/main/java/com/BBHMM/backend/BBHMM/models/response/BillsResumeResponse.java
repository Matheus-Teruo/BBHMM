package com.BBHMM.backend.BBHMM.models.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.BBHMM.backend.BBHMM.models.Bill;

public record BillsResumeResponse(
    UUID uuid,
    BigDecimal value
) {
    public BillsResumeResponse(Bill bill) {
        this(bill.getUuid(),
            bill.getValue()
        );
    }
}