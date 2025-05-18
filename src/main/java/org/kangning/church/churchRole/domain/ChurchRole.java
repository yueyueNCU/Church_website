package org.kangning.church.churchRole.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.ChurchRoleId;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class ChurchRole {
    private final ChurchRoleId id;
    private final ChurchId churchId;
    private final String name;
    private final boolean isDefault;
    private final Set<Permission> permissions;
}
