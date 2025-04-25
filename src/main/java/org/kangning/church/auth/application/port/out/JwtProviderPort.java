package org.kangning.church.auth.application.port.out;

import java.util.List;

public interface JwtProviderPort {
    String generateToken(String username, List<String> roles);
}
