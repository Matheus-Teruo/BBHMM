package com.BBHMM.backend.BBHMM.services.validation;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseInsertionException;
import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserValidation {

    private final UserRepository repository;

    public void checkNameDuplication(String username, String fullname, String email) {
        if (username != null && repository.existsByUsername(username)) {
            throw new InvalidDatabaseInsertionException(
                "Campo duplicado",
                "nome de usuário já está em uso",
                "Nome de usuário",
                Map.of(
                    "username",
                    username
                )
            );
        }
        if (fullname != null && repository.existsByFullname(fullname)) {
            throw new InvalidDatabaseInsertionException(
                "Campo duplicado",
                "esse nome já foi cadastrado",
                "Nome completo",
                Map.of(
                    "fullname",
                    fullname
                )
            );
        }
        if (email != null && repository.existsByEmail(email)) {
            throw new InvalidDatabaseInsertionException(
                "Campo duplicado",
                "esse email já foi cadastrado",
                "Email",
                Map.of(
                    "email",
                    email
                )
            );
        }
    }

    public void checkUserAuthentication(UUID requestUuid, User user){
        if (!requestUuid.equals(user.getUuid())) {
            throw new InvalidDatabaseQueryException(
                "Usuário não autenticado corretamente",
                "usuário não tem mesmo código do request",
                "UUID",
                user.getFullname().split(" ")[0]
            );
        }
    }
}
