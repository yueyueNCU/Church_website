package org.kangning.church.membership.application.port.in;

import org.kangning.church.auth.domain.Role;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.domain.ChurchMemberStatus;

import java.util.Set;

public record MemberResult(
        UserId userId,
        String username,
        Set<Role> roles,
        ChurchMemberStatus status
) {
}
