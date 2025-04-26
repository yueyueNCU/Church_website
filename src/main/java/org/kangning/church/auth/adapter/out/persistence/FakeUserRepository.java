package org.kangning.church.auth.adapter.out.persistence;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.Role;
import org.kangning.church.auth.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FakeUserRepository implements UserRepositoryPort {
    private final PasswordEncoder passwordEncoder;

    private final Map<String, User> users = new HashMap<>();

    @PostConstruct
    public void init() {
        // 預先放兩個使用者
        users.put("john", new User(
                "john",
                passwordEncoder.encode("123456"), // 密碼用 encoder 加密
                List.of(Role.LEADER, Role.MEMBER)
        ));
        users.put("admin", new User(
                "admin",
                passwordEncoder.encode("adminpass"),
                List.of(Role.ADMIN)
        ));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public void save(User user) {
        users.put(user.getUsername(), user);
    }
}
