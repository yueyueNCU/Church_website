package org.kangning.church.auth.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.kangning.church.auth.adapter.out.persistence.entity.UserEntity;
import org.kangning.church.auth.adapter.out.persistence.mapper.UserMapper;
import org.kangning.church.auth.application.port.out.UserRepositoryPort;
import org.kangning.church.auth.domain.User;
import org.kangning.church.common.identifier.UserId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepositoryPort {
    private final H2UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        UserEntity saved = userRepository.save(userMapper.toEntity(user));
        return userMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return userRepository.findById(id.value()).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByAccount(String account) {
        return userRepository.findByAccount(account).map(userMapper::toDomain);
    }

    @Override
    public void deleteByAll() {
        userRepository.deleteAll();
    }
}
