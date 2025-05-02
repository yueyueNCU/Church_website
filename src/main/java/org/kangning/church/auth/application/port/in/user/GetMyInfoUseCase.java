package org.kangning.church.auth.application.port.in.user;

import org.kangning.church.auth.adapter.in.dto.password.UserInfoResponse;
import org.kangning.church.common.identifier.UserId;

public interface GetMyInfoUseCase {
    UserInfoResult getMyInfo(UserId userId);
}
