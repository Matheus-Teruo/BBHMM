package com.BBHMM.backend.BBHMM.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.BBHMM.backend.BBHMM.BbhmmApplicationTests;
import com.BBHMM.backend.BBHMM.factory.UserFactory;
import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.SignupUserRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateUserRequest;
import com.BBHMM.backend.BBHMM.repositories.UserRepository;
import com.BBHMM.backend.BBHMM.services.validation.BillValidation;
import com.BBHMM.backend.BBHMM.services.validation.UserValidation;

class UserServiceTest extends BbhmmApplicationTests {

    @MockitoBean
    private EmailService emailService;
    @MockitoBean
    private TokenService tokenService;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private UserValidation userValidation;
    @MockitoBean
    private BillValidation billValidation;
    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Test
    void createUser_success() {
        // given
        SignupUserRequest request = UserFactory.createRequest();

        when(passwordEncoder.encode(request.password()))
                .thenReturn("encoded-password");

        doNothing().when(userValidation)
                .checkNameDuplication(request.username(), request.fullname(), request.email());

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        User result = userService.createUser(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPix()).isNotNull();
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(request.password());
    }

    @Test
    void createUser_withoutPix_success() {
        // given
        SignupUserRequest request = UserFactory.createRequestWithoutPix();

        when(passwordEncoder.encode(request.password()))
                .thenReturn("encoded-password");

        doNothing().when(userValidation)
                .checkNameDuplication(request.username(), request.fullname(), request.email());

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        User result = userService.createUser(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPix()).isNull();
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(request.password());
    }

    @Test
    void readUser_success() {
        // given
        UUID userUuid = UUID.randomUUID();
        User mockUser = UserFactory.user(userUuid);

        when(userRepository.findByUuid(userUuid)).thenReturn(Optional.of(mockUser));

        // when
        User result = userService.getUser(userUuid, mockUser);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUuid()).isEqualTo(mockUser);
        verify(userRepository, times(1)).findByUuid(userUuid);
    }

    @Test
    void safeTakeUser_success() {
        // given
        UUID userUuid = UUID.randomUUID();
        User mockUser = UserFactory.user(userUuid);

        when(userRepository.findByUuid(userUuid)).thenReturn(Optional.of(mockUser));

        // when
        User result = userService.safeTakeUserByUuid(userUuid);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUuid()).isEqualTo(mockUser);
        verify(userRepository, times(1)).findByUuid(userUuid);
    }

    @Test
    void safeTakeUser_fail_notFound() {
        // given
        UUID userUuid = UUID.randomUUID();
        when(userRepository.findByUuid(userUuid)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> userService.safeTakeUserByUuid(userUuid))
            .isInstanceOf(InvalidDatabaseQueryException.class)
            .hasMessageContaining("usuário inexistente");
    }

    @Test
    void updateUser_sucess() {
        // given
        UUID userUuid = UUID.randomUUID();
        User existingUser = spy(UserFactory.user(userUuid));
        UpdateUserRequest request = UserFactory.updateRequest(userUuid);

        doNothing().when(userValidation).checkNameDuplication(
                existingUser.getUsername(),
                existingUser.getFullname(),
                existingUser.getEmail());
        when(userRepository.findByUuid(userUuid)).thenReturn(Optional.of(existingUser));

        // when
        User result = userService.updateUser(request, existingUser);

        // then
        assertThat(result).isNotNull();
        verify(existingUser).updateUser(request);
        verify(userRepository).save(existingUser);
    }


}
