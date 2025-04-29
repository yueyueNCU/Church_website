package org.kangning.church.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.in.user.GetMyInfoUseCase;
import org.kangning.church.auth.adapter.in.dto.password.UserInfoResponse;
import org.kangning.church.auth.application.port.in.user.UserInfoResult;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.exception.auth.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMyInfoService implements GetMyInfoUseCase {

    private final UserRepositoryPort userRepository;

    @Override
    public UserInfoResult getMyInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        return new UserInfoResult(
                user.getId(),
                user.getUsername(),
                user.getGlobalRoles()
        );
    }
}
