package org.kangning.church.churchRole.adapter.out;

import org.kangning.church.church.adapter.out.persistent.entity.ChurchEntity;
import org.kangning.church.churchRole.adapter.out.entity.ChurchRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface H2ChurchRoleRepository extends JpaRepository<ChurchRoleEntity, Long> {
    List<ChurchRoleEntity> findAllByChurch_Id(Long churchId);
    List<ChurchRoleEntity> findAllByChurch_IdAndIsDefaultTrue(Long churchId);
    boolean existsByChurch_IdAndName(Long churchId, String name);

    ChurchEntity findByChurch_id(Long churchId);
}
