package org.kangning.church.auth.application.port.in.user.password;

import org.kangning.church.common.identifier.UserId;

public interface GetMyInfoUseCase {
    UserInfoResult getMyInfo(UserId userId);
}
