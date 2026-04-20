package com.BBHMM.backend.BBHMM.validations;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.BBHMM.backend.BBHMM.BbhmmApplicationTests;
import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseInsertionException;
import com.BBHMM.backend.BBHMM.repositories.UserRepository;
import com.BBHMM.backend.BBHMM.services.validation.UserValidation;

public class UserValidationTest extends BbhmmApplicationTests {

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private UserValidation userValidation;

    @Test
    void validationFieldDuplication_sucess() {
        // given
        String username = "username";
        String fullname = "user name";
        String email = "email";
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByFullname(fullname)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // then
        assertThatCode(() ->
            userValidation.checkNameDuplication(username, fullname, email))
            .doesNotThrowAnyException();

        verify(userRepository).existsByUsername(username);
        verify(userRepository).existsByFullname(fullname);
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void validationUsernameDuplication_fail() {
        // given
        String username = "username";
        String fullname = "user name";
        String email = "email";
        when(userRepository.existsByUsername(username)).thenReturn(true);
        when(userRepository.existsByFullname(fullname)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // then
        assertThatThrownBy(() ->
            userValidation.checkNameDuplication(username, fullname, email))
            .isInstanceOf(InvalidDatabaseInsertionException.class)
            .hasMessageContaining("Nome de usuário já está em uso");
    }
}
