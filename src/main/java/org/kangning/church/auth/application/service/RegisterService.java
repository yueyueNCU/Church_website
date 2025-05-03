package org.kangning.church.auth.application.service;

import lombok.AllArgsConstructor;
import org.kangning.church.auth.application.port.in.user.register.RegisterCommand;
import org.kangning.church.auth.application.port.in.user.register.RegisterUseCase;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.exception.auth.AccountAlreadyExistException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterService implements RegisterUseCase {
    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterCommand command){
        if(userRepository.existByAccount(command.account())) throw new AccountAlreadyExistException();

        userRepository.save(new User(
                null,
                command.username(),
                command.account(),
                passwordEncoder.encode(command.password()),
                null
            )
        );
    }
}
