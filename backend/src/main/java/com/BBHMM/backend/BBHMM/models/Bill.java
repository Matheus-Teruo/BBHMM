package com.BBHMM.backend.BBHMM.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

import com.BBHMM.backend.BBHMM.models.request.CreateBillRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateBillRequest;

@Entity
@Table(name = "bills")
@Getter
@NoArgsConstructor
public class Bill {

    @Id @GeneratedValue(generator = "UUID")
    private UUID uuid;

    @Column(nullable = false)
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "uuid_payer", nullable = false)
    private User payer;

    @ManyToOne
    @JoinColumn(name = "uuid_event", nullable = false)
    private Event event;

    @Column(name = "debit_amount",nullable = false)
    private BigDecimal debitAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean paid = false;

    @Setter
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participants> participants = new ArrayList<>();

    public Bill(CreateBillRequest request, Event event, User user) {
        this.value = request.value();
        this.event = event;
        this.payer = user;
    }

    public void update(UpdateBillRequest request) {
        if(request.value() != null) {
            this.value = request.value();
        }
    }
}
