package org.kangning.church.church.application.port.in;

public record CreateChurchCommand(
        String name,
        String address,
        String description
) {
}
