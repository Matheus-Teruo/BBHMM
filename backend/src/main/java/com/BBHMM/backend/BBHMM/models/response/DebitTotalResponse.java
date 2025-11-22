package com.BBHMM.backend.BBHMM.models.response;

import java.math.BigDecimal;

public record DebitTotalResponse(
    Boolean debit,
    BigDecimal value
) {
}
