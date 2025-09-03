package com.BBHMM.backend.BBHMM.models.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.BBHMM.backend.BBHMM.models.Bill;

public record BillResponse(
    UUID uuid,
    BigDecimal value,
    List<ParticipantResponse> participants
) {
    public BillResponse(Bill bill) {
        this(bill.getUuid(),
            bill.getValue(),
            bill.getParticipants().stream().map(ParticipantResponse::new).toList()
        );
    }
}
