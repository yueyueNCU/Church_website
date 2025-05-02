package org.kangning.church.auth.adapter.in.dto.login;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message="帳號不可為空")
        String account,

        @NotBlank(message = "密碼不能為空")
        String password
){}
