package com.BBHMM.backend.BBHMM.factory;

import com.BBHMM.backend.BBHMM.models.Bill;
import com.BBHMM.backend.BBHMM.models.request.CreateBillRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateBillRequest;

import java.math.BigDecimal;
import java.util.UUID;

public final class BillFactory {
    private static final String NAME = "Conta1";
    private static final String NAME_UPDATED = "Conta2";
    private static final String DESCRIPTION = "Description1";
    private static final String DESCRIPTION_UPDATED = "Description2";
    private static final BigDecimal VALUE = BigDecimal.TEN;
    private static final BigDecimal VALUE_UPDATED = BigDecimal.valueOf(15);
    
    public static Bill bill(UUID billUuid) {
        return Bill.builder()
            .uuid(billUuid)
            .name(NAME)
            .description(DESCRIPTION)
            .value(VALUE)
            .build();
    }

    public static CreateBillRequest createRequest(UUID payerUuid, UUID eventUuid) {
        return new CreateBillRequest(
            NAME,
            DESCRIPTION,
            VALUE,
            eventUuid,
            payerUuid
        );
    }

    public static UpdateBillRequest updateRequest(UUID billUuid) {
        return new UpdateBillRequest(
            billUuid,
            NAME_UPDATED,
            DESCRIPTION_UPDATED,
            VALUE_UPDATED,
            null
        );
    }
}
