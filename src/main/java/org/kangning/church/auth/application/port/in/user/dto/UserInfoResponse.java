package org.kangning.church.auth.application.port.in.user.dto;

import org.kangning.church.auth.domain.Role;

import java.util.List;

public record UserInfoResponse(
        String username,
        List<Role> globalRoles,
        List<ChurchRoleInfo> userChurchRoles
) {
    public record ChurchRoleInfo(
            Long churchId,
            List<Role> roles
    ) {}
}
