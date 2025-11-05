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
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(name = "uuid_payer", insertable = false, updatable = false)
    private UUID payerUuid;

    @ManyToOne
    @JoinColumn(name = "uuid_payer", nullable = false)
    private User payer;

    @ManyToOne
    @JoinColumn(name = "uuid_event", nullable = false)
    private Event event;

    @Setter
    @Column(name = "debit_amount",nullable = false)
    private BigDecimal debitAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean paid = false;

    private BillType type = BillType.BILL;

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

    public BigDecimal getRemainingBalance() {
        return this.value.subtract(this.debitAmount);
    }

    public void addToDebit(BigDecimal paymentAmount) {
        this.debitAmount = debitAmount.add(paymentAmount);
    }

    public void subtractToDebit(BigDecimal paymentAmount) {
        this.debitAmount = debitAmount.subtract(paymentAmount);
        this.paid = false;
    }

    public void completeBill() {
        this.debitAmount = this.value;
        this.paid = true;
    }

    public void undoCompleteBill() {
        this.debitAmount = BigDecimal.ZERO;
        this.paid = false;
    }
}
