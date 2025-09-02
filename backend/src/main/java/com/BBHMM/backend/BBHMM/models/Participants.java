package com.BBHMM.backend.BBHMM.models;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "participants")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Participants {

    @Id @GeneratedValue(generator = "UUID")
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "uuid_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "uuid_bill", nullable = false)
    private Bill bill;

    @Column(nullable = false)
    private Double paidValue = 0.0;

    @Column(nullable = false)
    private Boolean paid = false;
}
