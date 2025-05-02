package org.kangning.church.auth.application.port.in.user;

import org.kangning.church.auth.adapter.in.dto.password.UpdatePasswordRequest;
import org.kangning.church.common.identifier.UserId;

public interface UpdatePasswordUseCase {
    void updatePassword(UserId userId, UpdatePasswordCommand command);
}
