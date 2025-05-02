package org.kangning.church.church.adapter.out.persistent;

import lombok.RequiredArgsConstructor;
import org.kangning.church.church.adapter.out.persistent.entity.ChurchEntity;
import org.kangning.church.church.adapter.out.persistent.mapper.ChurchMapper;
import org.kangning.church.church.application.port.out.ChurchRepositoryPort;
import org.kangning.church.church.domain.Church;
import org.kangning.church.common.identifier.ChurchId;
import org.kangning.church.common.identifier.UserId;
import org.kangning.church.membership.domain.Membership;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaChurchRepository implements ChurchRepositoryPort {


    private final H2ChurchRepository churchRepository;
    private final ChurchMapper churchMapper;

    @Override
    public Church save(Church church) {
        ChurchEntity saved = churchRepository.save(churchMapper.toEntity(church));
        return churchMapper.toDomain(saved);
    }

    @Override
    public Optional<Church> findById(ChurchId id) {
        return churchRepository.findById(id.value())
                .map(churchMapper::toDomain);
    }

    @Override
    public List<Church> findAllByIds(List<ChurchId> ids) {
        List<Long> primaryKeys = ids.stream()
                .map(ChurchId::value)
                .toList();

        return churchRepository.findAllById(primaryKeys).stream()
                .map(churchMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(ChurchId id) {
        churchRepository.deleteById(id.value());
    }

    @Override
    public boolean existsByName(String name) {
        return churchRepository.existsByName(name);
    }

    @Override
    public List<Church> searchByNameContaining(String keyword, int limit, int offset) {
        PageRequest page = PageRequest.of(offset / limit, limit);
        return churchRepository.searchByNameContaining(keyword, page).stream()
                .map(churchMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteByAll() {
        churchRepository.deleteAll();
    }
}
