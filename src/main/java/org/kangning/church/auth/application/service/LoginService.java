package org.kangning.church.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.in.login.LoginCommand;
import org.kangning.church.auth.application.port.in.login.LoginResult;
import org.kangning.church.auth.application.port.in.login.LoginUseCase;
import org.kangning.church.auth.application.port.out.JwtProviderPort;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.exception.auth.PasswordIncorrectException;
import org.kangning.church.common.exception.auth.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {
    private final UserRepositoryPort userRepository;
    private final JwtProviderPort jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResult login(LoginCommand command) {
        User user= userRepository.findByAccount(command.account())
                .orElseThrow(UserNotFoundException::new);

        if(!passwordEncoder.matches(command.rawPassword(), user.getPasswordHash())){
            throw new PasswordIncorrectException();
        }
        String token = jwtProvider.generateToken(user.getId(),user.getUsername(),
                user.getGlobalRoles());

        return new LoginResult(token);
    }
}
