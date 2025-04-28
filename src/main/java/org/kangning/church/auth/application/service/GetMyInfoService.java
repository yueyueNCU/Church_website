package org.kangning.church.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.in.user.GetMyInfoUseCase;
import org.kangning.church.auth.application.port.in.user.dto.UserInfoResponse;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMyInfoService implements GetMyInfoUseCase {

    private final UserRepositoryPort userRepository;

    @Override
    public UserInfoResponse getMyInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        return new UserInfoResponse(
                user.getUsername(),
                user.getGlobalRoles(), // ✅ 全域權限
                user.getUserChurchRoles().stream()
                        .map(ucr -> new UserInfoResponse.ChurchRoleInfo(
                                ucr.getChurchId(),
                                ucr.getRoles()
                        ))
                        .toList()
        );
    }
}
