package org.kangning.church.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.in.user.UpdatePasswordUseCase;
import org.kangning.church.auth.application.port.in.user.dto.UpdatePasswordRequest;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.NewPasswordSameAsOldException;
import org.kangning.church.common.OldPasswordIncorrectException;
import org.kangning.church.common.PasswordMismatchException;
import org.kangning.church.common.UserNotFoundException;
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
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw new OldPasswordIncorrectException();
        }

        if(request.oldPassword().equals(request.newPassword())){
            throw new NewPasswordSameAsOldException();
        }
        if(!request.newPassword().equals(request.confirmPassword())) {
            throw new PasswordMismatchException();
        }
        String newHash = passwordEncoder.encode(request.newPassword());
        user.setPasswordHash(newHash);
        userRepository.save(user);
    }

}
