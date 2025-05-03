package org.kangning.church.auth.adapter.out.persistence;

import org.kangning.church.auth.adapter.out.persistence.entity.UserEntity;
import org.kangning.church.common.identifier.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface H2UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByAccount(String account);
    Boolean existsByAccount(String account);
}
