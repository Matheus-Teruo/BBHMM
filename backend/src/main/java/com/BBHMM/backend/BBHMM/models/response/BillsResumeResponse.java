package com.BBHMM.backend.BBHMM.models.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.BBHMM.backend.BBHMM.models.Bill;

public record BillsResumeResponse(
    UUID uuid,
    String billName,
    UUID payerUuid,
    BigDecimal value,
    List<UUID> participantsUuid
) {
    public BillsResumeResponse(Bill bill, List<UUID> participantsUuid) {
        this(bill.getUuid(),
            bill.getName(),
            bill.getPayerUuid(),
            bill.getValue(),
            participantsUuid
        );
    }
}