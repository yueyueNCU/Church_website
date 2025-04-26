package org.kangning.church.auth.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kangning.church.auth.application.port.in.login.dto.LoginRequest;
import org.kangning.church.auth.application.port.out.JwtProviderPort;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

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
        User mockUser = new User("john", "encoded-password", List.of(Role.LEADER));
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("123456", "encoded-password")).thenReturn(true);
        when(jwtProvider.generateToken(eq("john"), any())).thenReturn("mock-token");

        LoginRequest request = new LoginRequest("john", "123456");

        // Act
        var response = loginService.login(request);

        // Assert
        assertEquals("mock-token", response.token());
    }
    @Test
    void login_user_not_exist_should_return_exception(){
        when(userRepository.findByUsername("notexist")).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest("notexist", "123456");

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            loginService.login(request);
        });

        assertEquals("使用者不存在", thrown.getMessage());
    }

    @Test
    void login_user_password_wrong_should_return_exception(){
        User mockUser = new User("john", "encoded-password", List.of(Role.LEADER));
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("123456", "encoded-password")).thenReturn(false);
        LoginRequest request = new LoginRequest("john", "123456");

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            loginService.login(request);
        });

        assertEquals("密碼錯誤", thrown.getMessage());
    }
}