package org.kangning.church.auth.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kangning.church.auth.application.port.in.user.UpdatePasswordCommand;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.domain.ChurchMemberStatus;
import org.kangning.church.auth.domain.ChurchRole;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.exception.auth.NewPasswordSameAsOldException;
import org.kangning.church.common.exception.auth.OldPasswordIncorrectException;
import org.kangning.church.common.exception.auth.PasswordMismatchException;
import org.kangning.church.common.exception.auth.UserNotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdatePasswordServiceTest {

    @Mock
    private UserRepositoryPort  userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdatePasswordService updatePasswordService;

    @Test
    void updatePassword_user_not_exist_should_return_exception(){
        when(userRepository.findByUsername("not exist")).thenReturn(Optional.empty());

        UpdatePasswordCommand updatePasswordCommand=new UpdatePasswordCommand("oldPassword", "newPassword", "confirmPassword");
        assertThrows(UserNotFoundException.class, () -> {
            updatePasswordService.updatePassword("not exist", updatePasswordCommand);
        });
    }
    @Test
    void updatePassword_old_password_wrong_should_return_exception(){
        User mockUser = new User(
                new UserId(1L),
                "john",
                "encoded-password",
                Set.of(Role.SITE_ADMIN)
        );
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrongOldPassword", "encoded-password")).thenReturn(false);

        UpdatePasswordCommand updatePasswordCommand=new UpdatePasswordCommand("wrongOldPassword", "newPassword", "confirmPassword");
        assertThrows(OldPasswordIncorrectException.class, () -> {
            updatePasswordService.updatePassword("john", updatePasswordCommand);
        });
    }
    @Test
    void updatePassword_new_password_same_old_password_should_return_exception(){
        User mockUser = new User(
                new UserId(1L),
                "john",
                "encoded-password",
                Set.of(Role.SITE_ADMIN)
        );
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("oldPassword", "encoded-password")).thenReturn(true);

        UpdatePasswordCommand updatePasswordCommand=new UpdatePasswordCommand("oldPassword", "oldPassword", "confirmPassword");
        assertThrows(NewPasswordSameAsOldException.class, () -> {
            updatePasswordService.updatePassword("john", updatePasswordCommand);
        });
    }
    @Test
    void updatePassword_new_password_not_same_confirm_password_should_return_exception(){
        User mockUser = new User(
                new UserId(1L),
                "john",
                "encoded-password",
                Set.of(Role.SITE_ADMIN)
        );
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("oldPassword", "encoded-password")).thenReturn(true);

        UpdatePasswordCommand updatePasswordCommand=new UpdatePasswordCommand("oldPassword", "newPassword", "differentConfirmPassword");
        assertThrows(PasswordMismatchException.class, () -> {
            updatePasswordService.updatePassword("john", updatePasswordCommand);
        });
    }
    @Test
    void updatePassword_success_should_update_password(){
        User mockUser = new User(
                new UserId(1L),
                "john",
                "encoded-password",
                Set.of(Role.SITE_ADMIN)
        );
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("oldPassword", "encoded-password")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("hashed-new-password");


        UpdatePasswordCommand updatePasswordCommand=new UpdatePasswordCommand("oldPassword", "newPassword", "newPassword");
        updatePasswordService.updatePassword("john", updatePasswordCommand);

        assertEquals("hashed-new-password", mockUser.getPasswordHash()); // 確認 user 的密碼被更新了
    }

}