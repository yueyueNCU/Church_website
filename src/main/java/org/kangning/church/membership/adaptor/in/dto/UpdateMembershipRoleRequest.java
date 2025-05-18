package org.kangning.church.membership.adaptor.in.dto;

import org.kangning.church.auth.domain.Role;
import org.kangning.church.churchRole.domain.ChurchRole;

import java.util.Set;

public record UpdateMembershipRoleRequest(
        Set<ChurchRole> roles
) {}
