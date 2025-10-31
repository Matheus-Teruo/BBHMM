package com.BBHMM.backend.BBHMM.models;

public enum BillType {
    BILL("bill", false),
    PAYMENT("payment", true);

    private final String billTypeLower;
    private final boolean isPaymentBoolean;

    BillType(String billTypeLower, boolean isPaymentBoolean) {
        this.billTypeLower = billTypeLower;
        this.isPaymentBoolean = isPaymentBoolean;
    }

    public static BillType fromString(String type) {
        for(BillType billType : BillType.values()) {
        if (billType.billTypeLower.equalsIgnoreCase(type)){
            return billType;
        }
        }
        throw new IllegalArgumentException("Tipo não aceito");
    }

    public boolean isPaymentBoolean() {
        return this.isPaymentBoolean;
    }
}
