package org.kangning.church.church.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kangning.church.common.identifier.ChurchId;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Church {
    private ChurchId id;
    private String name;
    private String address;
    private String description;
    private Instant createdAt;
}
