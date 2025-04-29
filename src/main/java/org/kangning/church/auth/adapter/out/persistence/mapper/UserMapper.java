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

        // ① 先把 Set<String> 轉成 Set<Role>
        Set<Role> globalRoles = toRoleEnumSet(e.getGlobalRoles());

        // ② 再建 Domain User
        return new User(
                new UserId(e.getId()),
                e.getUsername(),
                e.getPasswordHash(),
                globalRoles
        );
    }

    /* ========= Domain → Entity ========= */
    public UserEntity toEntity(User d) {
        if (d == null) return null;

        UserEntity e = new UserEntity();
        e.setId(d.getId() == null ? null : d.getId().value());  // id 為 null 代表新建
        e.setUsername(d.getUsername());
        e.setPasswordHash(d.getPasswordHash());
        e.setGlobalRoles(fromRoleEnumSet(d.getGlobalRoles()));
        // UserEntity 不存 memberships，保持聚合分離
        return e;
    }

    /* ========= Helpers ========= */
    private Set<Role> toRoleEnumSet(Set<String> src) {
        return src == null
                ? Set.of()
                : src.stream()
                .map(Role::valueOf)                // "PASTOR" → Role.PASTOR
                .collect(Collectors.toSet());
    }
    private Set<String> fromRoleEnumSet(Set<Role> src) {
        return src == null
                ? Set.of()                              // 傳進來是 null 就回傳空集合
                : src.stream()
                .map(Enum::name)                   // Role.LEADER → "LEADER"
                .collect(Collectors.toSet());      // 收成 HashSet<String>
    }
}
