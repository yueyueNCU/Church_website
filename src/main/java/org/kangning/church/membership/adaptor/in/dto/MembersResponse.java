package org.kangning.church.membership.adaptor.in.dto;

import org.kangning.church.auth.domain.Role;
import org.kangning.church.churchRole.domain.ChurchRole;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.domain.ChurchMemberStatus;

import java.util.Set;

public record MembersResponse(
        UserId userId,
        String username,
        Set<ChurchRole> roles,
        ChurchMemberStatus status
) {
}
