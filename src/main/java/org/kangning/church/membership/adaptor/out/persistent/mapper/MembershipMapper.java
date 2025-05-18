package org.kangning.church.membership.adaptor.out.persistent.mapper;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.adapter.out.persistence.H2UserRepository;
import org.kangning.church.auth.adapter.out.persistence.entity.UserEntity;
import org.kangning.church.church.adapter.out.persistent.H2ChurchRepository;
import org.kangning.church.church.adapter.out.persistent.entity.ChurchEntity;
import org.kangning.church.church.application.port.out.ChurchRepositoryPort;
import org.kangning.church.churchRole.adapter.out.mapper.ChurchRoleMapper;
import org.kangning.church.churchRole.domain.ChurchRole;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.MembershipId;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.adaptor.out.persistent.H2MembershipRepository;
import org.kangning.church.membership.adaptor.out.persistent.entity.MembershipEntity;
import org.kangning.church.membership.domain.Membership;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MembershipMapper {

    private final ChurchRoleMapper roleMapper;
    /* ========= Entity → Domain ========= */
    public Membership toDomain(MembershipEntity e) {
        if (e == null) return null;

        return new Membership(
                new MembershipId(e.getId()),
                new ChurchId(e.getChurch().getId()),
                new UserId(e.getUser().getId()),
                e.getRoles().stream().map(roleMapper::toDomain).collect(Collectors.toSet()),
                e.getStatus()
        );
    }

    /* ========= Domain → Entity ========= */
    public MembershipEntity toEntity(Membership d) {
        MembershipEntity e = new MembershipEntity();
        e.setId(d.getId() != null ? d.getId().value() : null);
        e.setStatus(d.getStatus());
        return e;
    }
}