package org.windat.domain.service;

import jakarta.transaction.Transactional;
import org.windat.domain.entity.Lobby;
import org.windat.domain.repository.LobbyRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public class LobbyService implements LobbyFacade {

    private LobbyRepository lobbyRepository;

    public LobbyService(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    @Override
    public Collection<Lobby> readAll(){
        return lobbyRepository.readAll();
    }

    @Override
    @Transactional
    public Lobby create(Lobby lobby) {
        Date now = new Date();
        lobby.setCreated(now);
        lobby.setUpdated(now);


//      Set the lobby closure time so it will be next day (in 24 hours)
        Instant closedInstant = now.toInstant().plus(1, ChronoUnit.DAYS);
        Date closedDate = Date.from(closedInstant);
        lobby.setClosed(closedDate);

        return lobbyRepository.create(lobby);
    }


    @Override
    public Optional<Lobby> readOne(Integer lobbyId) {
        return lobbyRepository.readOne(lobbyId);
    }

}
