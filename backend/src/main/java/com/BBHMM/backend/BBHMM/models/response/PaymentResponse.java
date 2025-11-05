package com.BBHMM.backend.BBHMM.models.response;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponse(
    UUID userToPayUuid,
    BigDecimal value,
    UUID userToReceiveUuid
) {
}
