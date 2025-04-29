package org.kangning.church.church.application.port.in;


import org.kangning.church.common.identifier.ChurchId;

import java.time.Instant;

public record ChurchResult(
        ChurchId id,
        String name,
        String address,
        String description,
        Instant createdAt
) {
}
