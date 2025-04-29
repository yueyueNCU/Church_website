package org.kangning.church.church.adapter.in.dto;

import org.kangning.church.common.identifier.ChurchId;

import java.time.Instant;

public record ChurchResponse(
        ChurchId id,
        String name,
        String address,
        String description,
        Instant createdAt
) {
}
