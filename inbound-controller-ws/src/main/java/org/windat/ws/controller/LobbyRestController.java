package org.windat.ws.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.windat.domain.entity.Lobby;
import org.windat.domain.service.LobbyFacade;
import org.windat.rest.api.LobbiesApi;
import org.windat.rest.dto.LobbyCreateRequestDTODto;
import org.windat.rest.dto.LobbyDto;
import org.windat.ws.mapper.LobbyMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LobbyRestController implements LobbiesApi{

    private final LobbyFacade lobbyFacade;
    private final LobbyMapper lobbyMapper;

    public LobbyRestController(LobbyFacade lobbyFacade, LobbyMapper lobbyMapper) {
        this.lobbyFacade = lobbyFacade;
        this.lobbyMapper = lobbyMapper;
    }

    @Override
    public ResponseEntity<LobbyDto> createLobby(LobbyCreateRequestDTODto lobbyCreateRequestDTODto) {
        return null;
    }

    @Override
    public ResponseEntity<LobbyDto> getOneLobby(Integer lobbyId) {
        return null;
    }

    @Override
    public ResponseEntity<List<LobbyDto>> listLobbies() {
        Collection<Lobby> lobbies = lobbyFacade.readAll();
        List<LobbyDto> lobbyDtos = lobbies.stream()
                .map(lobbyMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lobbyDtos);
    }
}
