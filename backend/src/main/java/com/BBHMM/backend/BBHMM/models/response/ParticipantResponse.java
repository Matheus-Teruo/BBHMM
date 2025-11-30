package com.BBHMM.backend.BBHMM.models.response;

import com.BBHMM.backend.BBHMM.models.Participants;

import java.math.BigDecimal;
import java.util.UUID;

public record ParticipantResponse(
    UUID uuid,
    BigDecimal value,
    BigDecimal paid_value,
    Boolean paid,
    UUID userUuid
) {
    public ParticipantResponse(Participants participants) {
        this(participants.getUuid(),
            participants.getValue(),
            participants.getPaidValue(),
            participants.getPaid(),
            participants.getUserUuid()
        );
    }
}
