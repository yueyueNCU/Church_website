package org.kangning.church.churchRole.adapter.out.mapper;

import org.kangning.church.church.adapter.out.persistent.entity.ChurchEntity;
import org.kangning.church.church.domain.Church;
import org.kangning.church.churchRole.adapter.out.entity.ChurchRoleEntity;
import org.kangning.church.churchRole.domain.ChurchRole;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.ChurchRoleId;
import org.springframework.stereotype.Component;

@Component
public class ChurchRoleMapper {

    public ChurchRole toDomain(ChurchRoleEntity entity) {
        return new ChurchRole(
                new ChurchRoleId(entity.getId()),
                new ChurchId(entity.getChurch().getId()),
                entity.getName(),
                entity.isDefault(),
                entity.getPermissions()
        );
    }

    public ChurchRoleEntity toEntity(ChurchRole domain) {
        ChurchRoleEntity entity = new ChurchRoleEntity();
        entity.setId((domain.getId() != null) ? domain.getId().value() : null);
        entity.setName(domain.getName());
        entity.setDefault(domain.isDefault());
        entity.setPermissions(domain.getPermissions());

        return entity;
    }
}