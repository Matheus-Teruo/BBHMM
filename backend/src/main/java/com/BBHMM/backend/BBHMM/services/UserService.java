package com.BBHMM.backend.BBHMM.services;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.UserCreateRequest;
import com.BBHMM.backend.BBHMM.models.request.UserUpdateRequest;
import com.BBHMM.backend.BBHMM.repositories.UserRepository;
import com.BBHMM.backend.BBHMM.services.validation.UserValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final UserValidation validation;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(UserCreateRequest request) {
        validation.checkNameDuplication(request.username(), request.fullname());
        var user = new User(
            request.username(),
            passwordEncoder.encode(request.password()),
            request.fullname()
        );
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

    public List<User> findParticipantsByEventUuid(UUID eventUuid) {
        var users = repository.listUsersByEvent(eventUuid);

        return users;
    }

    @Transactional
    public User updateUser(UserUpdateRequest request) {
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

        return user;
    }
}
