package org.kangning.church.auth.application.port.in.user;

import org.kangning.church.auth.adapter.in.dto.password.UpdatePasswordRequest;

public interface UpdatePasswordUseCase {
    void updatePassword(String username, UpdatePasswordCommand command);
}
