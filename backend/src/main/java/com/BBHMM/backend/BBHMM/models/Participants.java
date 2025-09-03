package com.BBHMM.backend.BBHMM.models;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "participants")
@Getter
@NoArgsConstructor
public class Participants {

    @Id @GeneratedValue(generator = "UUID")
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "uuid_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "uuid_bill", nullable = false)
    private Bill bill;

    @Setter
    @Column(nullable = false)
    private BigDecimal value = BigDecimal.ZERO;

    @Setter
    @Column(name = "paid_value", nullable = false)
    private BigDecimal paidValue = BigDecimal.ZERO;

    @Setter
    @Column(nullable = false)
    private Boolean paid = false;

    public Participants(User user, Bill bill) {
        this.user = user;
        this.bill = bill;
    }
}
