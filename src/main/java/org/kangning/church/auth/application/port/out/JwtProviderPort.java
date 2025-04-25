package org.kangning.church.auth.application.port.out;

import org.kangning.church.auth.domain.Role;

import java.util.List;

public interface JwtProviderPort {
    String generateToken(String username, List<String> roles);
    String extractUsername(String token);
    List<Role> extractRoles(String token);
}
