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

    public void checkNameDuplication(String username, String fullname) {
        if (username != null && repository.existsByUsername(username)) {
            throw new InvalidDatabaseInsertionException(
                "Campo duplicado",
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
                "Nome completo",
                Map.of(
                    "fullname",
                    fullname
                )
            );
        }
    }

    public void checkUserAuthentication(UUID requestUuid, User user){
        if (!requestUuid.equals(user.getUuid())) {
            throw new InvalidDatabaseQueryException(
                "Usuário não autenticado corretamente",
                "ID",
                requestUuid.toString()
            );
        }
    }
}
