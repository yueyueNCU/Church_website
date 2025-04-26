package org.kangning.church.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.in.user.GetMyInfoUseCase;
import org.kangning.church.auth.application.port.in.user.dto.UserInfoResponse;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMyInfoService implements GetMyInfoUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserInfoResponse getMyInfo(String username) {
        return userRepositoryPort.findByUsername(username)
                .map(user -> new UserInfoResponse(
                        user.getUsername(),
                        user.getRoles()
                ))
                .orElseThrow(() -> new RuntimeException("使用者不存在"));
    }
}
