package org.windat.ws.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.windat.domain.entity.Lobby;
import org.windat.domain.service.LobbyFacade;
import org.windat.rest.api.LobbiesApi;
import org.windat.rest.dto.LobbyCreateRequestDTODto;
import org.windat.rest.dto.LobbyDto;
import org.windat.ws.mapper.LobbyMapper;

import java.util.ArrayList;
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
        Lobby newLobby = this.lobbyMapper.dtoToEntity(lobbyCreateRequestDTODto);
        Lobby savedLobby = this.lobbyFacade.create(newLobby);
        LobbyDto lobbyDto = this.lobbyMapper.toDto(savedLobby);
        return ResponseEntity.ok(lobbyDto);
    }

    @Override
    public ResponseEntity<LobbyDto> getOneLobby(Integer lobbyId) {
        if (this.lobbyFacade.readOne(lobbyId).isPresent()) {
            Lobby retrievedLobby = this.lobbyFacade.readOne(lobbyId).get();
            LobbyDto lobbyDto = this.lobbyMapper.toDto(retrievedLobby);
            return ResponseEntity.ok(lobbyDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<LobbyDto>> listLobbies() {
        Collection<Lobby> lobbies = lobbyFacade.readAll();
        List<LobbyDto> lobbyDtos = new ArrayList<>();

        if (lobbies != null) {
            lobbyDtos = lobbies.stream()
                    .map(lobbyMapper::toDto)
                    .collect(Collectors.toList());
        }
//        If lobbyFacade.readAll will return null, then this method will return 200 OK with
//        Empty array
//        If it is not null, then there will be array with lobbies
        return ResponseEntity.ok(lobbyDtos);
    }
}
