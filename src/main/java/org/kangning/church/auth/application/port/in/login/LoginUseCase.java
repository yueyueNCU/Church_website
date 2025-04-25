package org.kangning.church.auth.application.port.in.login;

import org.kangning.church.auth.application.port.in.login.dto.LoginRequest;
import org.kangning.church.auth.application.port.in.login.dto.LoginResponse;

public interface LoginUseCase {
    LoginResponse login(LoginRequest request);
}
