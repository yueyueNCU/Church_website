package org.kangning.church.auth.adapter.in.dto.password;

import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.domain.ChurchMemberStatus;
import org.kangning.church.auth.domain.Role;

import java.util.List;
import java.util.Set;

public record UserInfoResponse(
        UserId id,
        String username,
        Set<Role> globalRoles
) {
}
