package com.BBHMM.backend.BBHMM.services.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Token;
import com.BBHMM.backend.BBHMM.models.TokenType;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.repositories.TokenRespository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TokenValidation {

    private final TokenRespository respository;

    public void checkLastValidToken(User user, TokenType tokenType, LocalDateTime limitTime) {
        if (respository.checkLastValidByUserAndType(user.getUuid(), tokenType, limitTime)) {
            throw new InvalidDatabaseQueryException(
                "Token não pode ser criado",
                "já existe outro token existente, aguarde um momento antes de enviar outro.",
                "token",
                tokenType.toString());
        }
    }

    public void checkExpireToken(TokenType tokenType, Token token, LocalDateTime now ) {
        switch (tokenType) {
            case TokenType.CONFIRM_EMAIL:
                if (now.isAfter(token.getTimestamp().plusMinutes(3))) {
                    throw new InvalidDatabaseQueryException(
                        "Token expirado",
                        "tente enviar outra requisição",
                        "token",
                        tokenType.toString());
                }
                break;
            case TokenType.RESET_PASSWORD:
                if (now.isAfter(token.getTimestamp().plusMinutes(10))) {
                    throw new InvalidDatabaseQueryException(
                        "Token expirado",
                        "tente enviar outra requisição",
                        "token",
                        tokenType.toString());
                }
                break;
            default:
                throw new InvalidDatabaseQueryException(
                    "Token não classificado",
                    "token não tem uma classificação",
                    "token",
                    tokenType.toString());
        }
    }
}
