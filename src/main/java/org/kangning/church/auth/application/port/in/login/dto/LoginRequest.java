package org.kangning.church.auth.application.port.in.login.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message="用戶名稱不能為空")
        String username,

        @NotBlank(message = "密碼不能為空")
        String password
){}
