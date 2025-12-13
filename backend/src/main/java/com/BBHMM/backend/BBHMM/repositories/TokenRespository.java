package com.BBHMM.backend.BBHMM.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.BBHMM.backend.BBHMM.models.Token;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRespository extends JpaRepository<Token, UUID> {
    
    @Query("""
        SELECT t FROM Token t
        WHERE t.token = :token
        AND t.user.uuid = :userUuid
        AND t.timestamp >= :limitTime
    """)
    Optional<Token> findByTokenAndUserUuid(String token, UUID userUuid, LocalDateTime limitTime);
}
