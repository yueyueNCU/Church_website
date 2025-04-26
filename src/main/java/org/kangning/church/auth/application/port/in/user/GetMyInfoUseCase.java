package org.kangning.church.auth.application.port.in.user;

import org.kangning.church.auth.application.port.in.user.dto.UserInfoResponse;

public interface GetMyInfoUseCase {
    UserInfoResponse getMyInfo(String username);
}
