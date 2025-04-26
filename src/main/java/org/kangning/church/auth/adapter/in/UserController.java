package org.kangning.church.auth.adapter.in;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.in.user.GetMyInfoUseCase;
import org.kangning.church.auth.application.port.in.user.dto.UserInfoResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final GetMyInfoUseCase getMyInfoUseCase;

    @GetMapping("/me")
    public UserInfoResponse getMyInfo(Authentication authentication){
        return getMyInfoUseCase.getMyInfo(authentication.getName());
    }
    @GetMapping("/test")
    @PreAuthorize("hasRole('LEADER')")
    public UserInfoResponse test(Authentication authentication){
        return getMyInfoUseCase.getMyInfo(authentication.getName());
    }

}
