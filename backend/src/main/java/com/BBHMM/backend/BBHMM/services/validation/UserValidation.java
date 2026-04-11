package com.BBHMM.backend.BBHMM.services.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.BBHMM.backend.BBHMM.infra.exceptions.FieldErrorDetail;
import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseInsertionException;
import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.repositories.UserRepository;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserValidation {

    private final UserRepository repository;

    public void checkNameDuplication(String username, String fullname, String email) {
        if (username != null && repository.existsByUsername(username)) {
            throw new InvalidDatabaseInsertionException(
                "Nome de usuário duplicado",
                "Nome de usuário já está em uso",
                "User",
                List.of(
                    new FieldErrorDetail(
                        "username",
                        username)
                )
            );
        }
        if (fullname != null && repository.existsByFullname(fullname)) {
            throw new InvalidDatabaseInsertionException(
                "Nome duplicado",
                "Esse nome já foi cadastrado",
                "Nome completo",
                List.of(
                    new FieldErrorDetail(
                        "fullname",
                        fullname)
                )
            );
        }
        if (email != null && repository.existsByEmail(email)) {
            throw new InvalidDatabaseInsertionException(
                "Email duplicado",
                "Esse email já foi cadastrado",
                "Email",
                List.of(
                    new FieldErrorDetail(
                        "email",
                        email)
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

    public void checkUserEmailValidation(User user, boolean valid) {
        if (user.isEmailVerified() ^ valid) {
            throw new InvalidDatabaseQueryException(
                valid ? "Usuário com email não validado" : "Usuário com email já validado",
                valid ? "usuário ainda não tem o email validado" : "usuário já tem o email validado" ,
                "usuário",
                user.getFullname().split(" ")[0]
            );
        }
    }

    public void checkIsNotAGuest(User user) {
        if (repository.existsByIdAndRoleGuest(user.getUuid())) {
            throw new InvalidDatabaseInsertionException(
                "Não pode convidar usuário",
                "convidados só podem pertencer ao evento que foram criados",
                "usuário",
                List.of(
                    new FieldErrorDetail(
                        "userField",
                        user.getFullname())
                    )
            );
        }
    }
}
