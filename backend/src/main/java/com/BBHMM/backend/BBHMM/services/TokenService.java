package com.BBHMM.backend.BBHMM.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Token;
import com.BBHMM.backend.BBHMM.models.TokenType;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.repositories.TokenRespository;
import com.BBHMM.backend.BBHMM.services.validation.TokenValidation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRespository respository;
    private final TokenValidation validation;
    
    public Token createToken(TokenType type, User user) {
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
        respository.save(token);
        return token;
    }

    public void validateToken(TokenType type ,String tokenValue, UUID userUuid) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limitTime = now.minusMinutes(10);
        Token token = respository.findByTokenAndUserUuid(tokenValue, userUuid, limitTime)
        .orElseThrow(() -> new InvalidDatabaseQueryException(
            "Token não encontrado",
            "token pode ter expirado ou não existe, tente enviar outra requisição",
            "ID",
            tokenValue)
        );
        validation.checkExpireToken(type, token, now);
    }

    public static String generate6NumberToken() {
        SecureRandom secureRandom = new SecureRandom();
        int bound = (int) Math.pow(10, 6);
        int token = secureRandom.nextInt(bound);

        return String.format("%06d", token);
    }
}
