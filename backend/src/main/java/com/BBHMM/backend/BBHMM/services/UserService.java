package com.BBHMM.backend.BBHMM.services;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Pix;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.CreateGuestRequest;
import com.BBHMM.backend.BBHMM.models.request.SignUpUserRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateGuestToUserRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateUserRequest;
import com.BBHMM.backend.BBHMM.repositories.UserRepository;
import com.BBHMM.backend.BBHMM.services.validation.BillValidation;
import com.BBHMM.backend.BBHMM.services.validation.UserValidation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";

    private final UserRepository repository;
    private final UserValidation validation;
    private final BillValidation billValidation;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(SignUpUserRequest request) {
        validation.checkNameDuplication(request.username(), request.fullname(), request.email());
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

    public User getUser(UUID uuid, User user) {
        validation.checkUserAuthentication(uuid, user);
        return repository.findByUuid(uuid)
            .orElseThrow(EntityNotFoundException::new);
    }

    public User safeTakeUserByUuid(UUID uuid) {
        return repository.findByUuid(uuid)
            .orElseThrow(() -> new InvalidDatabaseQueryException(
                "Usuário não encontrado",
                "usuário inexistente",
                "ID",
                uuid.toString())
            );
    }

    public User findUserByNameOrFullnameOrEmail(String userfield) {
        if (userfield.matches("^.+@.+\\..+$")) {
            return repository.findByEmail(userfield)
                    .orElseThrow(() -> new InvalidDatabaseQueryException(
                            "Usuário não encontrado",
                            "email não condiz com nenhum usuário",
                            "userfield",
                            userfield
                    ));
        }

        if (userfield.matches(".*\\s+.*")) {
            return repository.findByFullname(userfield)
                    .orElseThrow(() -> new InvalidDatabaseQueryException(
                            "Usuário não encontrado",
                            "nome completo não condiz com nenhum usuário",
                            "userfield",
                            userfield
                    ));
        }

        return repository.findByUsername(userfield)
                .orElseThrow(() -> new InvalidDatabaseQueryException(
                        "Usuário não encontrado",
                        "nome de usuário não condiz com nenhum usuário",
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
    public User updateUser(UpdateUserRequest request, User userSecurity) {
        var user = safeTakeUserByUuid(request.uuid());
        validation.checkUserAuthentication(request.uuid(), userSecurity);
        validation.checkNameDuplication(request.username(), request.fullname(), request.email());

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

    public User createGuest(CreateGuestRequest request, User hostUser, String password) {
        validation.checkNameDuplication(request.guestName(), request.guestName(), null);
        billValidation.checkUserParticipationInEvent(hostUser, request.eventUuid());

        User user = new User(request, password);

        repository.save(user);
        return user;
    }

    @Transactional
    public User updateGuestToUser(UpdateGuestToUserRequest request) {
        validation.checkNameDuplication(null, null, request.email());
        User guestUser = safeTakeUserByUuid(request.uuid());
        validation.checkAlreadyUser(guestUser);

        guestUser.updateGuestToUser(request);
        
        return guestUser;
    }

    public String generatePassword() {
        SecureRandom random = new SecureRandom();
        List<Character> password = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            String all = LETTERS + NUMBERS;
            password.add(all.charAt(random.nextInt(all.length())));
        }

        Collections.shuffle(password, random);

        StringBuilder sb = new StringBuilder();
        for (char c : password) {
            sb.append(c);
        }

        return sb.toString();
    }
}
