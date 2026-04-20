package com.BBHMM.backend.BBHMM.models;

import java.util.UUID;

import com.BBHMM.backend.BBHMM.models.request.CreatePixRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdatePixRequest;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pixes")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    public Pix(CreatePixRequest request, User user) {
        this.pixKey = request.pixKey();
        this.bankAccount = request.bankAccount();
        this.user = user;
    }

    public Pix(UpdatePixRequest request, User user) {
        if (request.pixKey() != null) {
            this.pixKey = request.pixKey();
        }
        if (request.bankAccount() != null) {
            this.bankAccount = request.bankAccount();
        }
        this.user = user;
    }
}
