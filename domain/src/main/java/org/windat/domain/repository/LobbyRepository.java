package org.windat.domain.repository;

import org.windat.domain.entity.Lobby;

import java.util.Collection;
import java.util.Optional;

public interface LobbyRepository {

    Collection<Lobby> readAll();

    Lobby create(Lobby lobby);

    Optional<Lobby> readOne(Integer id);
}
