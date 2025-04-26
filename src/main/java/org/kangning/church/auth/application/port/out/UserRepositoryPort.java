package org.kangning.church.auth.application.port.out;

import org.kangning.church.auth.domain.User;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByUsername(String username);

    void save(User user);
}
