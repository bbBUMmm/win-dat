package org.windat.jpa.adapter;

import org.windat.domain.entity.User;
import org.windat.domain.repository.UserRepository;
import org.windat.jpa.repository.UserSpringDataRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class JpaUserRepositoryAdapter implements UserRepository {

    private final UserSpringDataRepository userSpringDataRepository;

    public JpaUserRepositoryAdapter(UserSpringDataRepository userSpringDataRepository) {
        this.userSpringDataRepository = userSpringDataRepository;
    }

    @Override
    public Collection<User> readAll() {
        return userSpringDataRepository.findAll();
    }

    @Override
    public User create(User user) {
        return userSpringDataRepository.save(user);
    }

    @Override
    public Optional<User> readOne(UUID uuid) {
        return userSpringDataRepository.findByKeycloakId(uuid);
    }

    @Override
    public User update(User user) {
        return userSpringDataRepository.save(user);
    }

    @Override
    public Optional<User> readOne(Integer id) {
        return userSpringDataRepository.findById(id);
    }

    @Override
    public Collection<User> readBest10UsersByWins() {
        return userSpringDataRepository.findTop10ByOrderByGamesWonDesc();
    }
}
