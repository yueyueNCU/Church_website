package org.kangning.church.auth.adapter.in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.in.login.LoginCommand;
import org.kangning.church.auth.application.port.in.login.LoginResult;
import org.kangning.church.auth.application.port.in.login.LoginUseCase;
import org.kangning.church.auth.adapter.in.dto.login.LoginRequest;
import org.kangning.church.auth.adapter.in.dto.login.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoginUseCase loginUseCase;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest dto){

        LoginCommand command = new LoginCommand(dto.username(), dto.password());

        LoginResult result = loginUseCase.login(command);

        LoginResponse response = new LoginResponse(result.jwtToken());

        return ResponseEntity.ok(response);
    }
}
