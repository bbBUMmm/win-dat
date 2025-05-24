package org.windat.domain.service;

import org.windat.domain.entity.Lobby;

import java.util.Collection;
import java.util.Optional;


// Note for myself:
// Defines the operations that the application layer exposes to the outside world, or other parts of the application
public interface LobbyFacade {

    Collection<Lobby> readAll();

    Lobby create(Lobby lobby);

    Optional<Lobby> readOne(Integer lobbyId);
}
