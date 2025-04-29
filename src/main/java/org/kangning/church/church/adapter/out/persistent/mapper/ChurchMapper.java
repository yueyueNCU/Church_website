package org.kangning.church.church.adapter.out.persistent.mapper;

import org.kangning.church.church.adapter.out.persistent.entity.ChurchEntity;
import org.kangning.church.church.domain.Church;
import org.kangning.church.common.identifier.ChurchId;
import org.springframework.stereotype.Component;

@Component
public class ChurchMapper {
    public Church toDomain(ChurchEntity e) {
        return new Church(
                new ChurchId(e.getId()),
                e.getName(),
                e.getAddress(),
                e.getDescription(),
                e.getCreatedAt());
    }
    public ChurchEntity toEntity(Church d) {
        ChurchEntity e = new ChurchEntity();
        e.setId(d.getId() == null ? null : d.getId().value());
        e.setName(d.getName());
        e.setAddress(d.getAddress());
        e.setDescription(d.getDescription());
        e.setCreatedAt(d.getCreatedAt());
        return e;
    }
}