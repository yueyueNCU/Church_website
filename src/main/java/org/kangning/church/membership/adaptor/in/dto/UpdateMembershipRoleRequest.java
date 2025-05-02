package org.kangning.church.membership.adaptor.in.dto;

import org.kangning.church.auth.domain.Role;

import java.util.Set;

public record UpdateMembershipRoleRequest(
        Set<Role> roles
) {}
