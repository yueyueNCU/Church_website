package org.kangning.church.auth.adapter.in;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.in.login.LoginUseCase;
import org.kangning.church.auth.application.port.in.login.dto.LoginRequest;
import org.kangning.church.auth.application.port.in.login.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoginUseCase loginUseCase;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse response = loginUseCase.login(request);
        return ResponseEntity.ok(response);
    }
}
