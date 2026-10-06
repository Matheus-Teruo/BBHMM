package com.BBHMM.backend.BBHMM.models;

import com.BBHMM.backend.BBHMM.models.request.UpdateEventUserRequest;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "event_user")
@Getter
@NoArgsConstructor
public class EventUser {

    @EmbeddedId
    private EventUserId id = new EventUserId();

    @ManyToOne
    @MapsId("uuidUser")
    @JoinColumn(name = "uuid_user")
    private User user;

    @ManyToOne
    @MapsId("uuidEvent")
    @JoinColumn(name = "uuid_event")
    private Event event;

    @Column(name = "color", length = 7)
    private String color;

    @Column(name = "total_to_receive", nullable = false)
    private BigDecimal totalToReceive;

    @Column(name = "total_to_pay", nullable = false)
    private BigDecimal totalToPay;

    @Column(name = "total_discounts", nullable = false)
    private BigDecimal totalDiscounts;

    public EventUser(User user, Event event) {
        this.user = user;
        this.event = event;
        this.totalToReceive = BigDecimal.ZERO;
        this.totalToPay = BigDecimal.ZERO;
        this.totalDiscounts = BigDecimal.ZERO;
    }

    public UUID getUserUuid() {
        return id.getUuidUser();
    }

    public UUID getEventUuid() {
        return id.getUuidEvent();
    }

    public void update(UpdateEventUserRequest request) {
        if (request.color() != null) this.color = request.color();
    }

    public void addCredit(BigDecimal value) {
        this.totalToReceive = this.totalToReceive.add(value);
    }

    public void subtractCredit(BigDecimal value) {
        this.totalToReceive = this.totalToReceive.subtract(value);
    }

    public void addDebit(BigDecimal value) {
        this.totalToPay = this.totalToPay.add(value);
    }

    public void subtractDebit(BigDecimal value) {
        this.totalToPay = this.totalToPay.subtract(value);
    }
}
