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
        User user= userRepository.findByUsername(command.username())
                .orElseThrow(UserNotFoundException::new);
        System.out.println("使用者輸入的密碼：" + command.rawPassword());
        System.out.println("使用者密碼has：" + passwordEncoder.encode(command.rawPassword()));
        System.out.println("資料庫密碼Hash：" + user.getPasswordHash());
        System.out.println("密碼比對結果：" + passwordEncoder.matches(command.rawPassword(), user.getPasswordHash()));
        if(!passwordEncoder.matches(command.rawPassword(), user.getPasswordHash())){
            throw new PasswordIncorrectException();
        }
        String token = jwtProvider.generateToken(user.getUsername(),
                user.getGlobalRoles().stream().map(Enum::name).toList());

        return new LoginResult(token);
    }
}
