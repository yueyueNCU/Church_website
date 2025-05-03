package org.kangning.church.auth.adapter.in;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.adapter.in.security.UserPrincipal;
import org.kangning.church.auth.application.port.in.user.password.GetMyInfoUseCase;
import org.kangning.church.auth.application.port.in.user.password.UpdatePasswordCommand;
import org.kangning.church.auth.application.port.in.user.password.UpdatePasswordUseCase;
import org.kangning.church.auth.adapter.in.dto.password.UpdatePasswordRequest;
import org.kangning.church.auth.adapter.in.dto.password.UserInfoResponse;
import org.kangning.church.auth.application.port.in.user.password.UserInfoResult;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Transactional
public class UserController {

    private final GetMyInfoUseCase getMyInfoUseCase;

    private final UpdatePasswordUseCase updatePasswordUseCase;
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(Authentication authentication){
        UserPrincipal principal =(UserPrincipal) authentication.getPrincipal();

        UserInfoResult result= getMyInfoUseCase.getMyInfo(principal.id());

        var response = new UserInfoResponse(
                result.id(),
                result.username(),
                result.globalRoles()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @Valid @RequestBody UpdatePasswordRequest request,
            Authentication authentication) {

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        UpdatePasswordCommand command = new UpdatePasswordCommand(
                request.oldPassword(), request.newPassword(), request.confirmPassword());

        updatePasswordUseCase.updatePassword(principal.id(), command);

        return ResponseEntity.ok().build(); // 204 No Content
    }

}
