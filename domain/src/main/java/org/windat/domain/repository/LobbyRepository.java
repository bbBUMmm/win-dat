package org.windat.domain.repository;

import org.windat.domain.entity.Lobby;

import java.util.Collection;

public interface LobbyRepository {

    Collection<Lobby> readAll();

    Lobby create(Lobby lobby);
}
