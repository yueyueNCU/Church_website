package org.kangning.church.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.in.user.password.GetMyInfoUseCase;
import org.kangning.church.auth.application.port.in.user.password.UserInfoResult;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.exception.auth.UserNotFoundException;
import org.kangning.church.common.identifier.UserId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMyInfoService implements GetMyInfoUseCase {

    private final UserRepositoryPort userRepository;

    @Override
    public UserInfoResult getMyInfo(UserId userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return new UserInfoResult(
                user.getId(),
                user.getUsername(),
                user.getGlobalRoles()
        );
    }
}
