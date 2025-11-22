package com.BBHMM.backend.BBHMM.services;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Pix;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.SignUpUserRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateUserRequest;
import com.BBHMM.backend.BBHMM.repositories.UserRepository;
import com.BBHMM.backend.BBHMM.services.validation.BillValidation;
import com.BBHMM.backend.BBHMM.services.validation.UserValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserValidation validation;
    private final BillValidation billValidation;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(SignUpUserRequest request) {
        validation.checkNameDuplication(request.username(), request.fullname());
        User user = new User(
            request,
            passwordEncoder.encode(request.password())
        );
        if (request.pix() != null) {
            Pix newPix = new Pix(request.pix(), user);
            user.setPix(newPix);
        }
        repository.save(user);

        return user;
    }

    public User safeTakeUserByUuid(UUID uuid) {
    return repository.findByUuid(uuid)
        .orElseThrow(() -> new InvalidDatabaseQueryException(
            "Usuário não encontrado",
            "ID",
            uuid.toString())
        );
    }

    public User findUserByNameOrFullnameOrEmail(String userfield) {
    if (userfield.matches("^.+@.+\\..+$")) {
        return repository.findByEmail(userfield)
                .orElseThrow(() -> new InvalidDatabaseQueryException(
                        "Usuário não encontrado pelo email",
                        "userfield",
                        userfield
                ));
    }

    if (userfield.matches(".*\\s+.*")) {
        return repository.findByFullname(userfield)
                .orElseThrow(() -> new InvalidDatabaseQueryException(
                        "Usuário não encontrado pelo nome completo",
                        "userfield",
                        userfield
                ));
    }

    return repository.findByUsername(userfield)
            .orElseThrow(() -> new InvalidDatabaseQueryException(
                    "Usuário não encontrado pelo nome do usuário",
                    "userfield",
                    userfield
            ));
    }

    public List<User> findParticipantsByEventUuid(UUID eventUuid) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        billValidation.checkUserParticipationInEvent(user, eventUuid);
        var users = repository.listUsersByEvent(eventUuid);

        return users;
    }

    @Transactional
    public User updateUser(UpdateUserRequest request) {
        var user = safeTakeUserByUuid(request.uuid());
        validation.checkUserAuthentication(request.uuid(), user);
        validation.checkNameDuplication(request.username(), request.fullname());

        String newPassword = "";
        boolean newPasswordFlag = false;
        if (request.password() != null) {
            newPassword = request.password();
            newPasswordFlag = true;
        }
        user.updateUser(request,  passwordEncoder.encode(newPassword), newPasswordFlag);
        if (request.pix() != null) {
            user.getPix().update(request.pix());
        }

        return user;
    }
}
