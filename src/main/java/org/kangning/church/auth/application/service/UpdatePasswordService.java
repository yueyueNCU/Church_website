package org.kangning.church.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.in.user.UpdatePasswordUseCase;
import org.kangning.church.auth.application.port.in.user.dto.UpdatePasswordRequest;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatePasswordService implements UpdatePasswordUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updatePassword(String username, UpdatePasswordRequest request) {
        User user= userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("使用者不存在"));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw new RuntimeException("舊密碼錯誤");
        }

        if(request.oldPassword().equals(request.newPassword())){
            throw new RuntimeException("新密碼不能與舊密碼相同");
        }
        if(!request.newPassword().equals(request.confirmPassword())) {
            throw new RuntimeException("新密碼與確認密碼不相同");
        }
        String newHash = passwordEncoder.encode(request.newPassword());
        user.setPasswordHash(newHash);
        userRepository.save(user);
    }

}
