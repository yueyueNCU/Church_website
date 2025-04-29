package org.kangning.church.auth.adapter.in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.in.user.GetMyInfoUseCase;
import org.kangning.church.auth.application.port.in.user.UpdatePasswordCommand;
import org.kangning.church.auth.application.port.in.user.UpdatePasswordUseCase;
import org.kangning.church.auth.adapter.in.dto.password.UpdatePasswordRequest;
import org.kangning.church.auth.adapter.in.dto.password.UserInfoResponse;
import org.kangning.church.auth.application.port.in.user.UserInfoResult;
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
        UserInfoResult result= getMyInfoUseCase.getMyInfo(authentication.getName());

        return new UserInfoResponse(
                result.id(),
                result.username(),
                result.globalRoles()
        );
    }

    @PutMapping("/password")
    public void updatePassword(
            @Valid @RequestBody UpdatePasswordRequest request,
            Authentication authentication) {

        UpdatePasswordCommand command = new UpdatePasswordCommand(request.oldPassword(), request.newPassword(), request.confirmPassword());

        updatePasswordUseCase.updatePassword(authentication.getName(), command);
    }

}
