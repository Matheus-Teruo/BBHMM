package com.BBHMM.backend.BBHMM.services.validation;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Token;
import com.BBHMM.backend.BBHMM.models.TokenType;

@Component
public class TokenValidation {
    public void checkExpireToken(TokenType tokenType, Token token, LocalDateTime now ) {
        switch (tokenType) {
            case TokenType.CONFIRM_EMAIL:
                if (now.plusMinutes(3).isAfter(token.getTimestamp())) {
                new InvalidDatabaseQueryException(
                    "Token expirado",
                    "tente enviar outra requisição",
                    "token",
                    tokenType.toString());
                }
                break;
            case TokenType.RESET_PASSWORD:
                if (now.plusMinutes(10).isAfter(token.getTimestamp())) {
                new InvalidDatabaseQueryException(
                    "Token expirado",
                    "tente enviar outra requisição",
                    "token",
                    tokenType.toString());
                }
                break;
            default:
                new InvalidDatabaseQueryException(
                    "Token não classificado",
                    "token não tem uma classificação",
                    "token",
                    tokenType.toString());
                break;
        }
    }
}
