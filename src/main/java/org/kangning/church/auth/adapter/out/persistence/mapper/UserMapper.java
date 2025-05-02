package org.kangning.church.auth.adapter.out.persistence.mapper;

import org.kangning.church.auth.adapter.out.persistence.entity.UserEntity;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.identifier.UserId;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    /* ========= Entity → Domain ========= */
    public User toDomain(UserEntity e) {
        if (e == null) return null;

        return new User(
                new UserId(e.getId()),
                e.getUsername(),
                e.getAccount(),
                e.getPasswordHash(),
                e.getGlobalRoles()
        );
    }

    /* ========= Domain → Entity ========= */
    public UserEntity toEntity(User d) {
        if (d == null) return null;

        UserEntity e = new UserEntity();
        e.setId(d.getId() == null ? null : d.getId().value());  // id 為 null 代表新建
        e.setAccount(d.getAccount());
        e.setUsername(d.getUsername());
        e.setPasswordHash(d.getPasswordHash());
        e.setGlobalRoles(d.getGlobalRoles());
        // UserEntity 不存 memberships，保持聚合分離
        return e;
    }
}
