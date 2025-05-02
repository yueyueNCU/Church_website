package org.kangning.church.church.adapter.out.persistent.mapper;

import org.kangning.church.auth.adapter.out.persistence.entity.UserEntity;
import org.kangning.church.auth.domain.User;
import org.kangning.church.church.adapter.out.persistent.entity.ChurchEntity;
import org.kangning.church.church.domain.Church;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.UserId;
import org.springframework.stereotype.Component;

@Component
public class ChurchMapper {
    public Church toDomain(ChurchEntity e) {
        if (e == null) return null;

        return new Church(
                new ChurchId(e.getId()),
                e.getName(),
                e.getAddress(),
                e.getDescription(),
                e.getCreatedAt()
        );
    }

    /* ========= Domain → Entity ========= */
    public ChurchEntity toEntity(Church d) {
        if (d == null) return null;

        ChurchEntity e = new ChurchEntity();
        e.setId(d.getId() == null ? null : d.getId().value());  // id 為 null 代表新建
        e.setName(d.getName());
        e.setAddress(d.getAddress());
        e.setDescription(d.getDescription());
        e.setCreatedAt(d.getCreatedAt());
        // UserEntity 不存 memberships，保持聚合分離
        return e;
    }
}