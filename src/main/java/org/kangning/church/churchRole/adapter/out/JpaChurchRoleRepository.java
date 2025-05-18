package org.kangning.church.churchRole.adapter.out;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.kangning.church.church.adapter.out.persistent.H2ChurchRepository;
import org.kangning.church.church.adapter.out.persistent.entity.ChurchEntity;
import org.kangning.church.churchRole.adapter.out.entity.ChurchRoleEntity;
import org.kangning.church.churchRole.adapter.out.mapper.ChurchRoleMapper;
import org.kangning.church.churchRole.application.port.out.ChurchRoleRepositoryPort;
import org.kangning.church.churchRole.domain.ChurchRole;
import org.kangning.church.common.exception.churchRole.ChurchRoleNotFoundException;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.ChurchRoleId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
@Transactional
public class JpaChurchRoleRepository implements ChurchRoleRepositoryPort {

    private final H2ChurchRoleRepository churchRoleRepository;

    private final H2ChurchRepository churchRepository;

    private final ChurchRoleMapper mapper;

    @Override
    public Set<ChurchRole> findAllByChurchId(ChurchId churchId) {
        List<ChurchRoleEntity> entities = churchRoleRepository.findAllByChurch_Id(churchId.value());
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ChurchRole> findDefaultRolesByChurchId(ChurchId churchId) {
        List<ChurchRoleEntity> entities = churchRoleRepository.findAllByChurch_IdAndIsDefaultTrue(churchId.value());
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public ChurchRole save(ChurchRole role) {
        ChurchRoleEntity entity = mapper.toEntity(role);
        ChurchEntity churchEntity= churchRepository.findById(role.getChurchId().value())
                .orElseThrow(ChurchRoleNotFoundException::new);

        entity.setChurch(churchEntity);
        ChurchRoleEntity saved = churchRoleRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Set<ChurchRole> saveAll(Set<ChurchRole> roles) {
        if (roles.isEmpty()) return Set.of();

        // 從任意一個 role 中取得 ChurchId
        ChurchId churchId = roles.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Role set is empty"))
                .getChurchId();
        // 取得 ChurchEntity（關聯用）
        ChurchEntity churchEntity = churchRepository.findById(churchId.value())
                .orElseThrow(ChurchRoleNotFoundException::new);

        // 為每個角色建構 entity 並設好 church
        List<ChurchRoleEntity> entities = roles.stream()
                .map(role -> {
                    ChurchRoleEntity entity = mapper.toEntity(role);
                    entity.setChurch(churchEntity);  // 關聯到該教會
                    return entity;
                })
                .toList();

        // 批次儲存
        List<ChurchRoleEntity> savedEntities = churchRoleRepository.saveAll(entities);
        // 回傳轉換後的 domain
        return savedEntities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteById(ChurchRoleId id) {
        churchRoleRepository.deleteById(id.value());
    }

    @Override
    public Optional<ChurchRole> findById(ChurchRoleId id) {
        return churchRoleRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByChurchIdAndName(ChurchId churchId, String name) {
        return churchRoleRepository.existsByChurch_IdAndName(churchId.value(), name);
    }
}
