package org.kangning.church.auth.adapter.in.dto.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(
        @NotBlank(message = "舊密碼不能為空")
        String oldPassword,
        @NotBlank(message = "新密碼不能為空")
        @Size(min = 8, message = "新密碼長度至少為8位")
        String newPassword,
        @NotBlank(message = "確認密碼不能為空")
        String confirmPassword
){}
