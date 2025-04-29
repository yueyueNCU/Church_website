package org.kangning.church.auth.application.port.in.login;

import org.kangning.church.auth.adapter.in.dto.login.LoginRequest;
import org.kangning.church.auth.adapter.in.dto.login.LoginResponse;

public interface LoginUseCase {
    LoginResult login(LoginCommand command);
}
