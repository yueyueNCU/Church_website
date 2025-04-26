package org.kangning.church.auth.application.port.in.user;

import org.kangning.church.auth.application.port.in.user.dto.UpdatePasswordRequest;

public interface UpdatePasswordUseCase {
    void updatePassword(String username, UpdatePasswordRequest request);
}
