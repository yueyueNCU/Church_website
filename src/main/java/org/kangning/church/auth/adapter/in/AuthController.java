package org.kangning.church.auth.adapter.in;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.adapter.in.dto.register.RegisterRequest;
import org.kangning.church.auth.application.port.in.login.LoginCommand;
import org.kangning.church.auth.application.port.in.login.LoginResult;
import org.kangning.church.auth.application.port.in.login.LoginUseCase;
import org.kangning.church.auth.adapter.in.dto.login.LoginRequest;
import org.kangning.church.auth.adapter.in.dto.login.LoginResponse;
import org.kangning.church.auth.application.port.in.user.register.RegisterCommand;
import org.kangning.church.auth.application.port.in.user.register.RegisterUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Transactional
@RequiredArgsConstructor
public class AuthController {
    private final LoginUseCase loginUseCase;

    private final RegisterUseCase registerUseCase;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest dto){

        LoginCommand command = new LoginCommand(dto.account(), dto.password());

        LoginResult result = loginUseCase.login(command);

        LoginResponse response = new LoginResponse(result.jwtToken());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        RegisterCommand command = RegisterCommand.of(request.username(),request.account(),request.password(),request.confirmPassword());
        registerUseCase.register(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
