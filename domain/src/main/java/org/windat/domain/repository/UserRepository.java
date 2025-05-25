package org.windat.domain.repository;

import org.windat.domain.entity.User;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Collection<User> readAll();

    User create(User user);

    Optional<User> readOne(UUID uuid);

    User update(User user);
}
