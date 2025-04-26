package org.kangning.church.auth.adapter.in;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.in.user.GetMyInfoUseCase;
import org.kangning.church.auth.application.port.in.user.UpdatePasswordUseCase;
import org.kangning.church.auth.application.port.in.user.dto.UpdatePasswordRequest;
import org.kangning.church.auth.application.port.in.user.dto.UserInfoResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final GetMyInfoUseCase getMyInfoUseCase;

    private final UpdatePasswordUseCase updatePasswordUseCase;

    @GetMapping("/me")
    public UserInfoResponse getMyInfo(Authentication authentication){
        return getMyInfoUseCase.getMyInfo(authentication.getName());
    }

    @PutMapping("/password")
    public void updatePassword(
            @RequestBody UpdatePasswordRequest request,
            Authentication authentication) {
        updatePasswordUseCase.updatePassword(authentication.getName(), request);
    }
}
