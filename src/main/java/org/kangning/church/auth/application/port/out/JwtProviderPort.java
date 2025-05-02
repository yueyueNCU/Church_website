package org.kangning.church.auth.application.port.out;

import org.kangning.church.auth.domain.Role;
import org.kangning.church.common.identifier.UserId;

import java.util.List;
import java.util.Set;

public interface JwtProviderPort {
    String extractUsername(String token);
    Set<Role> extractRoles(String token);
    String generateToken(UserId userId, String username, Set<Role> roles);
    UserId extractUserId(String token);
}
