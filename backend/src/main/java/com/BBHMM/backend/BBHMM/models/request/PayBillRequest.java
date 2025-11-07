package com.BBHMM.backend.BBHMM.models.request;

import java.math.BigDecimal;
import java.util.UUID;

public record PayBillRequest(
    UUID userToPayUuid,
    BigDecimal value,
    UUID userToReceiveUuid,
    UUID eventuUuid
) {
}
