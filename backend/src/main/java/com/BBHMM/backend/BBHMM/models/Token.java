package com.BBHMM.backend.BBHMM.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tokens")
@Getter
@NoArgsConstructor
public class Token {
    @Id @GeneratedValue(generator = "UUID")
    private UUID uuid;

    @Column(unique = true, nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private TokenType type;
    
    @Column(name = "time_stamp", nullable = false, unique = true, length = 50)
    private LocalDateTime timestamp;

    @Setter
    private boolean valid;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uuid_user", nullable = false)
    private User user;

    public Token(String token, TokenType type, User user) {
        this.token = token;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.valid = true;
        this.user = user;
    }
}
