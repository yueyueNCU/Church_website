package org.kangning.church.auth.adapter.in.dto.register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record RegisterRequest(
        @NotBlank(message = "使用者名稱不能為空")
        @Size(min = 3, message = "使用者名稱長度至少為3位")
        String username,
        @NotBlank(message = "帳號不能為空")
        @Size(min = 8, message = "帳號長度至少為8位")
        String account,
        @NotBlank(message = "密碼不能為空")
        @Size(min = 8, message = "密碼長度至少為8位")
        String password,
        @NotBlank(message = "確認密碼不能為空")
        @Size(min = 8, message = "密碼長度至少為8位")
        String confirmPassword

) {
}
