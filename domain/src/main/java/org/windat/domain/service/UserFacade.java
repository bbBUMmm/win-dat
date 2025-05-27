package org.windat.domain.service;

import org.windat.domain.entity.User;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UserFacade {

    Collection<User> readAll();

    User create(User user);

    Optional<User> readOne(UUID uuid);

    User update(User user);

//    Use this method to retrieve user from the application database
    Optional<User> readOne(Integer id);

    Collection<User> readBest10UsersByWins();
}
