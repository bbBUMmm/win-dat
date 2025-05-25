package org.windat.ws.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.windat.domain.UserRole;
import org.windat.domain.entity.Lobby;
import org.windat.domain.entity.User;
import org.windat.domain.exceptions.LobbyFullException;
import org.windat.domain.exceptions.LobbyNotFoundException;
import org.windat.domain.exceptions.UserAlreadyInLobbyException;
import org.windat.domain.service.LobbyFacade;
import org.windat.domain.service.UserFacade;
import org.windat.rest.api.LobbiesApi;
import org.windat.rest.dto.LobbyCreateRequestDTODto;
import org.windat.rest.dto.LobbyDto;
import org.windat.rest.dto.UserDto;
import org.windat.ws.mapper.LobbyMapper;
import org.windat.ws.mapper.UserMapper;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;

@RestController
public class LobbyRestController implements LobbiesApi {

    private final LobbyFacade lobbyFacade;
    private final LobbyMapper lobbyMapper;

    private final UserFacade userFacade;
    private final UserMapper userMapper;

    public LobbyRestController(
            LobbyFacade lobbyFacade,
            LobbyMapper lobbyMapper,
            UserFacade userFacade,
            UserMapper userMapper
    ) {
        this.lobbyFacade = lobbyFacade;
        this.lobbyMapper = lobbyMapper;
        this.userFacade = userFacade;
        this.userMapper = userMapper;
    }

    /**
     * Retrieve user from JWT token. If exists in application database, add to lobby.
     * If not, create this user in application database and then add to the lobby.
     *
     * @param lobbyId The ID of the lobby to add the user to.
     * @return ResponseEntity containing the updated LobbyDto or an error status.
     * @throws LobbyNotFoundException if the lobby does not exist.
     * @throws LobbyFullException if the lobby is full.
     * @throws UserAlreadyInLobbyException if the user is already in the lobby.
     */
    @Override
    public ResponseEntity<LobbyDto> addUserToLobby(Integer lobbyId) {

//        Get user from jwt token
        UserDto userDto = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID keycloakId = userDto.getKeycloakId();
        String username = userDto.getLoginName();

//        Get lobby or throw error if optional returns null
        Lobby lobby = lobbyFacade.readOne(lobbyId).orElseThrow(
                () -> new LobbyNotFoundException("Lobby with ID " + lobbyId + " not found.")
        );

//        Get users if exists in application database or create new if not
        User user = userFacade.readOne(keycloakId).orElseGet(() -> {
            User newUser = new User();
            newUser.setLoginName(username);
            newUser.setKeycloakId(keycloakId);
            newUser.setUserRoleEnum(UserRole.USER_ROLE);

            return userFacade.create(newUser);
        });

//        Check if lobby is full
        if (lobby.isFull()) {
            throw new LobbyFullException("Lobby is already full.");
        }

//        Check if user already exist in lobby
        if (lobby.containsUser(user)) {
            throw new UserAlreadyInLobbyException("User " + username + " is already in lobby.");
        }

        lobby.addUser(user);

//        Persist user updates
        userFacade.update(user);

//        Persist updated lobby
        Lobby updatedLobby = lobbyFacade.update(lobby);

        return ResponseEntity.ok(lobbyMapper.toDto(updatedLobby));
    }

    @Override
    public ResponseEntity<LobbyDto> createLobby(LobbyCreateRequestDTODto lobbyCreateRequestDTODto) {
        Lobby newLobby = this.lobbyMapper.dtoToEntity(lobbyCreateRequestDTODto);
        Lobby savedLobby = this.lobbyFacade.create(newLobby);
        LobbyDto lobbyDto = this.lobbyMapper.toDto(savedLobby);
        return ResponseEntity.status(HttpStatus.CREATED).body(lobbyDto);
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
