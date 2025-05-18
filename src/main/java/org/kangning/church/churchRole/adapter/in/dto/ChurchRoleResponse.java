package org.kangning.church.churchRole.adapter.in.dto;

import org.kangning.church.churchRole.domain.ChurchRole;
import org.kangning.church.churchRole.domain.Permission;

import java.util.Set;

public record ChurchRoleResponse(Long id, String name, boolean isDefault, Set<Permission> permissions) {
    public static ChurchRoleResponse from(ChurchRole role) {
        return new ChurchRoleResponse(
                role.getId().value(),
                role.getName(),
                role.isDefault(),
                role.getPermissions()
        );
    }
}