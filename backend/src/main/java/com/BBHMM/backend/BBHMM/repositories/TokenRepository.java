package com.BBHMM.backend.BBHMM.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.BBHMM.backend.BBHMM.models.Token;
import com.BBHMM.backend.BBHMM.models.TokenType;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {

    @EntityGraph(attributePaths = "user")
    Optional<Token> getByToken(String token);
    
    @Query("""
        SELECT t
        FROM Token t
        WHERE t.user.uuid = :userUuid
            AND t.type = :tokenType
            AND t.timestamp >= :limitTime
            AND t.valid = true
        ORDER BY t.timestamp DESC
    """)
    Optional<Token> findLastValidByUserAndType(
        UUID userUuid,
        TokenType tokenType,
        LocalDateTime limitTime
    );

    @Query("""
        SELECT CASE WHEN EXISTS (
        SELECT 1 FROM Token t
        WHERE t.user.uuid = :userUuid
        AND t.type = :tokenType
        AND t.timestamp >= :limitTime
        AND t.valid = true
        ) THEN TRUE ELSE FALSE END
    """)
    boolean checkLastValidByUserAndType(UUID userUuid, TokenType tokenType, LocalDateTime limitTime);
    
    @Query("""
        SELECT t FROM Token t
        WHERE t.token = :token
        AND t.user.uuid = :userUuid
        AND t.timestamp >= :limitTime
        AND t.valid = true
    """)
    Optional<Token> findByTokenAndUserUuid(String token, UUID userUuid, LocalDateTime limitTime);
}
