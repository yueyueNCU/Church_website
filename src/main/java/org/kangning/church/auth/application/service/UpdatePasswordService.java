package org.kangning.church.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.in.user.password.UpdatePasswordCommand;
import org.kangning.church.auth.application.port.in.user.password.UpdatePasswordUseCase;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.exception.auth.NewPasswordSameAsOldException;
import org.kangning.church.common.exception.auth.OldPasswordIncorrectException;
import org.kangning.church.common.exception.auth.PasswordMismatchException;
import org.kangning.church.common.identifier.UserId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.kangning.church.common.exception.auth.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class UpdatePasswordService implements UpdatePasswordUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updatePassword(UserId userId, UpdatePasswordCommand command) {
        User user= userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(command.oldPassword(), user.getPasswordHash())) {
            throw new OldPasswordIncorrectException();
        }

        if(command.oldPassword().equals(command.newPassword())){
            throw new NewPasswordSameAsOldException();
        }
        if(!command.newPassword().equals(command.confirmPassword())) {
            throw new PasswordMismatchException();
        }
        String newHash = passwordEncoder.encode(command.newPassword());
        user.setPasswordHash(newHash);
        userRepository.save(user);
    }

}
