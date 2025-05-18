package org.kangning.church.auth.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kangning.church.auth.application.port.in.login.LoginCommand;
import org.kangning.church.auth.application.port.out.JwtProviderPort;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.exception.auth.PasswordIncorrectException;
import org.kangning.church.common.exception.auth.UserNotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @Mock
    private UserRepositoryPort userRepository;
    @Mock
    private JwtProviderPort jwtProvider;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    @Test
    void login_success_should_return_token() {
        User mockUser = new User(
                new UserId(1L),
                "john",
                "TestAccount",
                "encoded-password",
                null
        );

        when(userRepository.findByAccount("TestAccount")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("12345678", "encoded-password")).thenReturn(true);
        when(jwtProvider.generateToken(eq(new UserId(1L)), any(),any())).thenReturn("mock-token");

        LoginCommand command = new LoginCommand("TestAccount", "12345678");

        // Act
        var response = loginService.login(command);

        // Assert
        assertEquals("mock-token", response.jwtToken());
    }
    @Test
    void login_user_not_exist_should_return_exception(){
        when(userRepository.findByAccount("not exist")).thenReturn(Optional.empty());

        LoginCommand command = new LoginCommand("not exist", "12345678");

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            loginService.login(command);
        });
    }

    @Test
    void login_user_password_wrong_should_return_exception(){
        User mockUser = new User(
                new UserId(1L),
                "john",
                "TestAccount",
                "encoded-password",
                null
        );
        when(userRepository.findByAccount("TestAccount")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);
        LoginCommand command = new LoginCommand("TestAccount", "wrong-password");

        // Act & Assert
        assertThrows(PasswordIncorrectException.class, () -> {
            loginService.login(command);
        });
    }
}