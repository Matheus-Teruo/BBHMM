package com.BBHMM.backend.BBHMM.models;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pix")
@Getter
@NoArgsConstructor
public class Pix {

    @Id @GeneratedValue(generator = "UUID")
    private UUID uuid;

    @Column(name = "pix_key", nullable = false, length = 100)
    private String pixKey;

    @Column(name = "bank_account" ,nullable = false, length = 100)
    private String bankAccount;

    @OneToOne
    @JoinColumn(name = "uuid_user", nullable = false)
    private User user;
}
