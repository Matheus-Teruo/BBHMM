package com.BBHMM.backend.BBHMM.factory;

import com.BBHMM.backend.BBHMM.models.Pix;
import com.BBHMM.backend.BBHMM.models.RoleEnum;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.CreatePixRequest;
import com.BBHMM.backend.BBHMM.models.request.SignupUserRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdatePixRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateUserRequest;

import java.util.UUID;

public final class UserFactory {
    private static final String USERNAME = "User1";
    private static final String USERNAME_UPDATED = "User2";
    private static final String PASSOWRD = "password1";
    private static final String PASSOWRD_UPDATED = "password2";
    private static final String FULLNAME = "User name 1";
    private static final String FULLNAME_UPDATED = "User name 2";
    private static final String EMAIL = "email1@test.com";
    private static final String EMAIL_UPDATED = "email2@test.com";
    private static final String PIX = "email1@pix.com";
    private static final String PIX_UPDATED = "email2@pix.com";
    private static final String BANK_ACCOUNT = "Bank 1 Spring Boot";
    private static final String BANK_ACCOUNT_UPDATED = "Bank 2 Spring Boot";
    
    public static User user(UUID userUuid) {
        User user = User.builder()
            .uuid(userUuid)
            .username(USERNAME)
            .password(PASSOWRD)
            .role(RoleEnum.ROLE_USER)
            .fullname(FULLNAME)
            .email(EMAIL)
            .build();
        user.setPix(pix(UUID.randomUUID(), user));
        return user;
    }

    public static Pix pix(UUID pixUuid, User user) {
        return Pix.builder()
            .uuid(pixUuid)
            .pixKey(PIX)
            .bankAccount(BANK_ACCOUNT)
            .user(user)
            .build();
    }

    public static SignupUserRequest createRequest() {
        return new SignupUserRequest(
            USERNAME,
            PASSOWRD,
            FULLNAME,
            EMAIL,
            createPixRequest()
        );
    }

    public static SignupUserRequest createRequestWithoutPix() {
        return new SignupUserRequest(
            USERNAME,
            PASSOWRD,
            FULLNAME,
            EMAIL,
            null
        );
    }

    public static CreatePixRequest createPixRequest() {
        return new CreatePixRequest(
            PIX,
            BANK_ACCOUNT
        );
    }

    public static UpdateUserRequest updateRequest(UUID userUuid) {
        return new UpdateUserRequest(
            userUuid,
            USERNAME_UPDATED,
            PASSOWRD_UPDATED,
            FULLNAME_UPDATED,
            EMAIL_UPDATED,
            updatePixRequest()
        );
    }

    public static UpdatePixRequest updatePixRequest() {
        return new UpdatePixRequest(
            PIX_UPDATED,
            BANK_ACCOUNT_UPDATED
        );
    }
}
