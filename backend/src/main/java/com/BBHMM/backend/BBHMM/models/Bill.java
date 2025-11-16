package com.BBHMM.backend.BBHMM.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

import com.BBHMM.backend.BBHMM.models.request.CreateBillRequest;
import com.BBHMM.backend.BBHMM.models.request.PayBillRequest;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uuid_payer", nullable = false)
    private User payer;

    @ManyToOne
    @JoinColumn(name = "uuid_event", nullable = false)
    private Event event;

    @Setter
    @Column(name = "debit_amount", nullable = false)
    private BigDecimal debitAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean paid = false;

    @Enumerated(EnumType.STRING)
    private BillType type = BillType.BILL;

    @Setter
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participants> participants = new ArrayList<>();

    public Bill(CreateBillRequest request, Event event, User user) {
        this.name = request.name();
        if (request.description() != null) {
            this.description = request.description();
        }
        this.value = request.value();
        this.event = event;
        this.payer = user;
    }

    public Bill(PayBillRequest request, Event event, User user) {
        this.name = "Pagamento";
        this.value = request.value();
        this.debitAmount = request.value();
        this.event = event;
        this.payer = user;
        this.type = BillType.PAYMENT;
        this.paid = true;
    }

    public void addParticipant(Participants participant) {
        participants.add(participant);
    }

    public void update(UpdateBillRequest request) {
        if (request.name() != null) {
            this.name = request.name();
        }
        if (request.description() != null) {
            this.description = request.description();
        }
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
