package org.kangning.church.auth.application.port.in.user;

import org.kangning.church.auth.domain.Role;
import org.kangning.church.common.identifier.UserId;

import java.util.Set;

public record UserInfoResult(
    UserId id,
    String username,
    Set<Role> globalRoles
) {
}
