package com.BBHMM.backend.BBHMM.models;

public enum RoleEnum {
    ROLE_USER("usuário", true),
    ROLE_GUEST("convidado", false);

    private final String userRoleLower;
    private final boolean typeBoolean;

    RoleEnum(String userRoleLower, boolean typeBoolean) {
        this.userRoleLower = userRoleLower;
        this.typeBoolean = typeBoolean;
    }

    public static RoleEnum fromString(String type) {
        for(RoleEnum userRoleLower : RoleEnum.values()) {
            if (userRoleLower.userRoleLower.equalsIgnoreCase(type)){
                return userRoleLower;
            }
        }
        throw new IllegalArgumentException("No role type found from the passed String");
    }

    public String toString() {
        return this.userRoleLower;
    }

    public boolean isUser() {
        return !this.typeBoolean;
    }
}
