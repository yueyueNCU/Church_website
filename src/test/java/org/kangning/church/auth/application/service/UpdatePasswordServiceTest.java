package org.kangning.church.auth.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kangning.church.auth.application.port.in.user.dto.UpdatePasswordRequest;
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

        UpdatePasswordRequest updatePasswordRequest=new UpdatePasswordRequest("oldPassword", "newPassword", "confirmPassword");
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            updatePasswordService.updatePassword("not exist", updatePasswordRequest);
        });
        assertEquals("使用者不存在", thrown.getMessage());
    }
    @Test
    void updatePassword_old_password_wrong_should_return_exception(){
        User mockUser = new User("john", "encoded-password", List.of(Role.LEADER));
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrongOldPassword", "encoded-password")).thenReturn(false);

        UpdatePasswordRequest updatePasswordRequest=new UpdatePasswordRequest("wrongOldPassword", "newPassword", "confirmPassword");
        RuntimeException thrown =assertThrows(RuntimeException.class, () -> {
            updatePasswordService.updatePassword("john", updatePasswordRequest);
        });
        assertEquals("舊密碼錯誤", thrown.getMessage());
    }
    @Test
    void updatePassword_new_password_same_old_password_should_return_exception(){
        User mockUser =new User("john", "encoded-password", List.of(Role.LEADER));
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("oldPassword", "encoded-password")).thenReturn(true);

        UpdatePasswordRequest updatePasswordRequest=new UpdatePasswordRequest("oldPassword", "oldPassword", "confirmPassword");
        RuntimeException thrown =assertThrows(RuntimeException.class, () -> {
            updatePasswordService.updatePassword("john", updatePasswordRequest);
            });
        assertEquals("新密碼不能與舊密碼相同", thrown.getMessage());
    }
    @Test
    void updatePassword_new_password_not_same_confirm_password_should_return_exception(){
        User mockUser =new User("john", "encoded-password", List.of(Role.LEADER));
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("oldPassword", "encoded-password")).thenReturn(true);

        UpdatePasswordRequest updatePasswordRequest=new UpdatePasswordRequest("oldPassword", "newPassword", "differentConfirmPassword");
        RuntimeException thrown =assertThrows(RuntimeException.class, () -> {
            updatePasswordService.updatePassword("john", updatePasswordRequest);
        });
        assertEquals("新密碼與確認密碼不相同", thrown.getMessage());
    }
    @Test
    void updatePassword_success_should_update_password(){
        User mockUser =new User("john", "encoded-password", List.of(Role.LEADER));
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("oldPassword", "encoded-password")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("hashed-new-password");


        UpdatePasswordRequest updatePasswordRequest=new UpdatePasswordRequest("oldPassword", "newPassword", "newPassword");
        updatePasswordService.updatePassword("john", updatePasswordRequest);

        assertEquals("hashed-new-password", mockUser.getPasswordHash()); // 確認 user 的密碼被更新了
    }

}