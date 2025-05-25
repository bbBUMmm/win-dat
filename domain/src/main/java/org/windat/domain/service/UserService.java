package org.windat.domain.service;

import jakarta.transaction.Transactional;
import org.windat.domain.entity.User;
import org.windat.domain.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService implements UserFacade{

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Collection<User> readAll() {
        return userRepository.readAll();
    }

    @Override
    @Transactional
    public User create(User user) {
        return userRepository.create(user);
    }

    @Override
    public Optional<User> readOne(UUID uuid) {
        return userRepository.readOne(uuid);
    }

    @Override
    @Transactional
    public User update(User user) {
        return userRepository.update(user);
    }

    @Override
    public Optional<User> readOne(Integer id) {
        return userRepository.readOne(id);
    }
}
