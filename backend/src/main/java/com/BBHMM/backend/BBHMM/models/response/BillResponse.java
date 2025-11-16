package com.BBHMM.backend.BBHMM.models.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.BBHMM.backend.BBHMM.models.Bill;

public record BillResponse(
    UUID uuid,
    String billName,
    String description,
    BigDecimal value,
    UserResponse payer,
    List<ParticipantResponse> participants
) {
    public BillResponse(Bill bill) {
        this(bill.getUuid(),
            bill.getName(),
            bill.getDescription(),
            bill.getValue(),
            new UserResponse(bill.getPayer()),
            bill.getParticipants().stream().map(ParticipantResponse::new).toList()
        );
    }
}
