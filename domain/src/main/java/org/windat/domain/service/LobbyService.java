package org.windat.domain.service;

import org.windat.domain.entity.Lobby;
import org.windat.domain.repository.LobbyRepository;

import java.util.Collection;

public class LobbyService implements LobbyFacade {

    private LobbyRepository lobbyRepository;

    public LobbyService(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    @Override
    public Collection<Lobby> readAll(){
        return lobbyRepository.readAll();
    }
}
