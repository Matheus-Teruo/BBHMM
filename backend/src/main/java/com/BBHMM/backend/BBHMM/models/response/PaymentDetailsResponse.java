package com.BBHMM.backend.BBHMM.models.response;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentDetailsResponse(
    UUID billUuid,
    UUID userToPayUuid,
    BigDecimal value,
    BigDecimal paidValue,
    boolean paid,
    UUID userToReceiveUuid
) {
}
