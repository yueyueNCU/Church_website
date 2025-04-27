package org.kangning.church.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.adapter.in.UserController;
import org.kangning.church.auth.application.port.in.login.LoginUseCase;
import org.kangning.church.auth.application.port.in.login.dto.LoginRequest;
import org.kangning.church.auth.application.port.in.login.dto.LoginResponse;
import org.kangning.church.auth.application.port.out.JwtProviderPort;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.PasswordIncorrectException;
import org.kangning.church.common.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {
    private final UserRepositoryPort userRepository;
    private final JwtProviderPort jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user= userRepository.findByUsername(request.username())
                .orElseThrow(UserNotFoundException::new);
        if(!passwordEncoder.matches(request.password(), user.getPasswordHash())){
            throw new PasswordIncorrectException();
        }
        String token = jwtProvider.generateToken(user.getUsername(),
                user.getRoles().stream().map(Enum::name).toList());

        return new LoginResponse(token);
    }
}
