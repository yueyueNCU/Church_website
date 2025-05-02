package org.kangning.church.membership.adaptor.out.persistent.mapper;

import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.MembershipId;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.adaptor.out.persistent.entity.MembershipEntity;
import org.kangning.church.membership.domain.Membership;
import org.springframework.stereotype.Component;

@Component
public class MembershipMapper {

    /* ========= Entity → Domain ========= */
    public Membership toDomain(MembershipEntity e) {
        if (e == null) return null;

        return new Membership(
                new MembershipId(e.getId()),
                new ChurchId(e.getChurchId()),
                new UserId(e.getUserId()),
                e.getRoles(),
                e.getStatus()
        );
    }

    /* ========= Domain → Entity ========= */
    public MembershipEntity toEntity(Membership d) {
        if (d == null) return null;

        MembershipEntity e = new MembershipEntity();
        e.setId(d.getId() == null ? null : d.getId().value());  // id 為 null 代表新建
        e.setChurchId(d.getChurchId().value());
        e.setUserId(d.getUserId().value());
        e.setRoles(d.getRoles());
        e.setStatus(d.getStatus());
        return e;
    }
}
