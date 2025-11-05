package com.BBHMM.backend.BBHMM.models;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class UserBalance {
    
    private UUID userUuid;

    @Setter
    private BigDecimal balance;
}