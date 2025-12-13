package com.BBHMM.backend.BBHMM.models;

public enum TokenType {
    CONFIRM_EMAIL("confirm-email"),
    RESET_PASSWORD("reset-password");

    private final String tokenLower;

    TokenType(String tokenLower) {
        this.tokenLower = tokenLower;
    }

    public static TokenType fromString(String type) {
        for(TokenType tokenLower : TokenType.values()) {
            if (tokenLower.tokenLower.equalsIgnoreCase(type)){
                return tokenLower;
            }
        }
        throw new IllegalArgumentException("No role type found from the passed String");
    }

    public String toString() {
        return this.tokenLower;
    }
}
