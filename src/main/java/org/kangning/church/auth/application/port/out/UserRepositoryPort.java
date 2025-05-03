package org.kangning.church.auth.application.port.out;


import org.kangning.church.auth.domain.User;
import org.kangning.church.common.identifier.UserId;

import java.util.Optional;

public interface UserRepositoryPort  {
    User save(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findById(UserId id);
    Optional<User> findByAccount(String account);
    void deleteByAll();
    Boolean existByAccount(String account);
}
