package com.BBHMM.backend.BBHMM.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.EventUser;
import com.BBHMM.backend.BBHMM.models.Pix;
import com.BBHMM.backend.BBHMM.models.Token;
import com.BBHMM.backend.BBHMM.models.TokenType;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.CheckResetPasswordRequest;
import com.BBHMM.backend.BBHMM.models.request.CreateGuestRequest;
import com.BBHMM.backend.BBHMM.models.request.EmailTokenRequest;
import com.BBHMM.backend.BBHMM.models.request.EmailValidationRequest;
import com.BBHMM.backend.BBHMM.models.request.ResetPasswordRequest;
import com.BBHMM.backend.BBHMM.models.request.SignupUserRequest;
import com.BBHMM.backend.BBHMM.models.request.UpgradeGuestToUserRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateUserRequest;
import com.BBHMM.backend.BBHMM.repositories.UserRepository;
import com.BBHMM.backend.BBHMM.services.validation.BillValidation;
import com.BBHMM.backend.BBHMM.services.validation.UserValidation;

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

    private final EmailService emailService;
    private final TokenService tokenService;
    private final UserRepository repository;
    private final UserValidation validation;
    private final BillValidation billValidation;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(SignupUserRequest request) {
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

    public User findUserByNameOrFullnameOrEmail(String userField) {
        if (userField.matches("^.+@.+\\..+$")) {
            return repository.findByEmail(userField)
                    .orElseThrow(() -> new InvalidDatabaseQueryException(
                            "Usuário não encontrado",
                            "email não condiz com nenhum usuário",
                            "email",
                            userField
                    ));
        }

        if (userField.matches(".*\\s+.*")) {
            return repository.findByFullname(userField)
                    .orElseThrow(() -> new InvalidDatabaseQueryException(
                            "Usuário não encontrado",
                            "nome completo não condiz com nenhum usuário",
                            "nome completo",
                            userField
                    ));
        }

        return repository.findByUsername(userField)
                .orElseThrow(() -> new InvalidDatabaseQueryException(
                        "Usuário não encontrado",
                        "nome de usuário não condiz com nenhum usuário",
                        "nome de usuário",
                        userField
                ));
    }

    private User findUserByEmail(String email) {
        return repository.findByEmail(email)
                    .orElseThrow(() -> new InvalidDatabaseQueryException(
                            "Usuário não encontrado",
                            "email não condiz com nenhum usuário",
                            "email",
                            email
                    ));
    }

    public List<EventUser> findParticipantsByEventUuid(UUID eventUuid) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        billValidation.checkUserParticipationInEvent(user, eventUuid);
        var eventUsers = repository.listUsersByEvent(eventUuid);

        return eventUsers;
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
        user.updateUser(request);
        if (newPasswordFlag) user.updatePassword(passwordEncoder.encode(newPassword));
        if (request.pix() != null) {
            user.setPix(new Pix(request.pix(), user));
        }

        return user;
    }

    public void verifyEmail(EmailValidationRequest request, User userSecurity) {
        validation.checkUserAuthentication(request.userUuid(), userSecurity);
        validation.checkUserEmailValidation(userSecurity, false);
        User user = safeTakeUserByUuid(request.userUuid());
        Token token = tokenService.createToken(TokenType.CONFIRM_EMAIL, user);

        emailService.sendValidationEmail(user, token.getToken());
    }

    @Transactional
    public void confirmEmail(EmailTokenRequest request, User userSecurity) {
        tokenService.validateToken(TokenType.CONFIRM_EMAIL, request.token(), userSecurity.getUuid());
        validation.checkUserEmailValidation(userSecurity, false);

        User user = safeTakeUserByUuid(userSecurity.getUuid());
        user.setEmailVerified(true);
    }

    public void resetPassword(ResetPasswordRequest request) {
        User user = findUserByEmail(request.email());
        validation.checkUserEmailValidation(user, true);
        Token token = tokenService.createToken(TokenType.RESET_PASSWORD, user);

        emailService.sendPasswordResetEmail(user, token.getToken());
    }

    @Transactional
    public User checkResetPassword(CheckResetPasswordRequest request, String password) {
        User user = tokenService.getUserByToken(request.token().toString());
        tokenService.validateToken(TokenType.RESET_PASSWORD, request.token().toString(), user.getUuid());
        user.updatePassword(passwordEncoder.encode(password));
        return user;
    }

    public User createGuest(CreateGuestRequest request, User hostUser, String password, Event event) {
        validation.checkNameDuplication(request.guestName(), request.guestName(), null);
        billValidation.checkUserParticipationInEvent(hostUser, request.eventUuid());
        billValidation.checkEventFinished(event);

        User guest = new User(request, passwordEncoder.encode(password));

        repository.save(guest);
        return guest;
    }

    @Transactional
    public User getGuest(UUID guestUuid, User hostUser, UUID eventUuid, String password) {
        billValidation.checkUsersParticipationInEvent(eventUuid, List.of(guestUuid, hostUser.getUuid()));
        User guest = safeTakeUserByUuid(guestUuid);

        guest.updatePassword(passwordEncoder.encode(password));

        return guest;
    }

    @Transactional
    public User upgradeGuestToUser(UpgradeGuestToUserRequest request) {
        validation.checkNameDuplication(null, null, request.email());
        User guest = safeTakeUserByUuid(request.uuid());

        guest.upgradeGuestToUser(request, passwordEncoder.encode(request.password()));
        
        return guest;
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
