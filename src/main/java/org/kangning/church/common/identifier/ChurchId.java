package org.kangning.church.common.identifier;

import org.kangning.church.common.domain.Identifier;

public record ChurchId(Long value) implements Identifier<Long> {
    @Override public String toString() { return String.valueOf(value); }
}
