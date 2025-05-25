package org.windat.jpa.adapter;

import org.springframework.stereotype.Repository;
import org.windat.domain.entity.Lobby;
import org.windat.domain.repository.LobbyRepository;
import org.windat.jpa.repository.LobbySpringDataRepository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class JpaLobbyRepositoryAdapter implements LobbyRepository {

    private final LobbySpringDataRepository lobbySpringDataRepository;

    public JpaLobbyRepositoryAdapter(LobbySpringDataRepository lobbySpringDataRepository) {
        this.lobbySpringDataRepository = lobbySpringDataRepository;
    }

    @Override
    public Collection<Lobby> readAll() {
        return lobbySpringDataRepository.findAll();
    }

    @Override
    public Lobby create(Lobby lobby) {
        return lobbySpringDataRepository.save(lobby);
    }


    @Override
    public Optional<Lobby> readOne(Integer id) {
        return lobbySpringDataRepository.findById(id);
    }

    @Override
    public Lobby update(Lobby lobby) {
        return lobbySpringDataRepository.save(lobby);
    }
}
