package org.kangning.church.auth.application.port.in.user;

import org.kangning.church.auth.adapter.in.dto.password.UserInfoResponse;

public interface GetMyInfoUseCase {
    UserInfoResult getMyInfo(String username);
}
