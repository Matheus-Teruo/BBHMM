package com.BBHMM.backend.BBHMM.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Token;
import com.BBHMM.backend.BBHMM.models.TokenType;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.repositories.TokenRepository;
import com.BBHMM.backend.BBHMM.services.validation.TokenValidation;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository repository;
    private final TokenValidation validation;
    
    @Transactional
    public Token createToken(TokenType type, User user) {
        LocalDateTime limitTime = LocalDateTime.now().minusMinutes(2);
        validation.checkLastValidToken(user, type, limitTime);

        LocalDateTime rangeTimeForLast = LocalDateTime.now().minusMinutes(10);
        Optional<Token> lastToken = repository.findLastValidByUserAndType(user.getUuid(), type, rangeTimeForLast);
        if (lastToken.isPresent()) {
            lastToken.get().setValid(false);
        }

        String tokenValue;
        switch (type) {
            case TokenType.CONFIRM_EMAIL:
                tokenValue = generate6NumberToken();
                break;

            case TokenType.RESET_PASSWORD:
                tokenValue = UUID.randomUUID().toString();
                break;
        
            default:
                tokenValue = "";
                break;
        }
        
        Token token = new Token(tokenValue, type, user);
        repository.save(token);
        return token;
    }

    public void validateToken(TokenType type ,String tokenValue, UUID userUuid) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limitTime = now.minusMinutes(10);
        Token token = repository.findByTokenAndUserUuid(tokenValue, userUuid, limitTime)
        .orElseThrow(() -> new InvalidDatabaseQueryException(
            "Token não encontrado",
            "token pode ter expirado ou não existe, tente enviar outra requisição",
            "ID",
            tokenValue)
        );
        validation.checkExpireToken(type, token, now);
    }

    public User getUserByToken(String tokenValue) {
        Token token = repository.getByToken(tokenValue).orElseThrow(() -> new InvalidDatabaseQueryException(
            "Token não encontrado",
            "token pode ter expirado ou não existe, tente enviar outra requisição",
            "token",
            tokenValue)
        );
        return token.getUser();
    }

    public static String generate6NumberToken() {
        SecureRandom secureRandom = new SecureRandom();
        int bound = (int) Math.pow(10, 6);
        int token = secureRandom.nextInt(bound);

        return String.format("%06d", token);
    }
}
