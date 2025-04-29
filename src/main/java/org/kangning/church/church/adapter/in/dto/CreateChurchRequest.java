package org.kangning.church.church.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateChurchRequest(
        @NotBlank(message = "教會名稱不能為空")
        @Size(min = 3, message = "教會名稱長度至少為3位")
        String name,
        @NotBlank(message = "地址不能為空")
        String address,
        String description
) {
}
