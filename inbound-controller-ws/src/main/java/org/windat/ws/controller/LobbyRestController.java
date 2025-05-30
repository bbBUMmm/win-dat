package org.windat.ws.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;
import org.windat.domain.entity.CreditTransaction;
import org.windat.domain.enums.TransactionType;
import org.windat.domain.enums.UserRole;
import org.windat.domain.entity.Lobby;
import org.windat.domain.entity.User;
import org.windat.domain.exceptions.*;
import org.windat.domain.service.CreditFacade;
import org.windat.domain.service.LobbyFacade;
import org.windat.domain.service.UserFacade;
import org.windat.rest.api.DuelResultsApi;
import org.windat.rest.api.DuelWinnerApi;
import org.windat.rest.api.LobbiesApi;
import org.windat.rest.dto.*;
import org.windat.ws.mapper.LobbyMapper;
import org.windat.ws.mapper.UserMapper;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;

@RestController
public class LobbyRestController implements LobbiesApi, DuelResultsApi, DuelWinnerApi {

    private final LobbyFacade lobbyFacade;
    private final LobbyMapper lobbyMapper;
    private final CreditFacade creditFacade;

    private final UserFacade userFacade;
    private final UserMapper userMapper;

    public LobbyRestController(
            LobbyFacade lobbyFacade,
            LobbyMapper lobbyMapper,
            UserFacade userFacade,
            UserMapper userMapper,
            CreditFacade creditFacade
    ) {
        this.lobbyFacade = lobbyFacade;
        this.lobbyMapper = lobbyMapper;
        this.userFacade = userFacade;
        this.userMapper = userMapper;
        this.creditFacade = creditFacade;
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

//      Check if user is currently in different lobby
        if (user.hasAnyLobby()){
            throw new UserAlreadyHasLobbyWithNameException("You are already in the lobby with name " +
                    user.getLobby().getName() + ".");
        }
//        Check if lobby is full
        if (lobby.isFull()) {
            throw new LobbyFullException("Lobby is already full.");
        }

//        Check if user already exist in lobby
        if (lobby.containsUser(user)) {
            throw new UserAlreadyInLobbyException("User " + username + " is already in lobby.");
        }

//        Check if user has enough credits to play in this lobby
        if (user.getCredits() <= lobby.getAmount()) {
            throw new UserDoesNotHaveEnoughBalanceException("You do not have enough credits to join this lobby. You need " +
                    (lobby.getAmount() - user.getCredits()) + " credits more.");
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

    /**
     * Allows an authenticated user to remove themselves from their current lobby.
     * The lobby ID is retrieved from the user's current association.
     *
     * @return ResponseEntity containing the updated LobbyDto (representing the lobby after removal)
     * or an error status.
     * @throws UserNotFoundException if the authenticated user is not found in the application database.
     * @throws UserIsNotInAnyLobbyException if the authenticated user is not currently associated with any lobby.
     */
    @Override
    public ResponseEntity<LobbyDto> removeAuthenticatedUserFromLobby() {

//        Get keycloak user from jwt token
        UserDto userDto = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID keycloakId = userDto.getKeycloakId();

//        Get application user from keycloakId
        User applicationUser = userFacade.readOne(keycloakId).orElseThrow(
                () -> new UserNotFoundException("User with " + keycloakId + " not found in the application database.")
        );

//        Check if user is in any lobby
        if (!applicationUser.hasAnyLobby()) {
            throw new UserIsNotInAnyLobbyException("User " + applicationUser.getLoginName() + " is not in any lobby.");
        }

//        Get lobby form the user
        Lobby lobby = applicationUser.getLobby();

//        Remove this user from the lobby
        lobby.removeUser(applicationUser);
//        Persist updated lobby
        lobbyFacade.update(lobby);

//        Explicitly set user lobby to null
        applicationUser.setLobby(null);
//        Persist updated user
        userFacade.update(applicationUser);

        return ResponseEntity.ok(lobbyMapper.toDto(lobby));
    }

    /**
     * Allows an administrator to remove a specific user from a specific lobby.
     * This operation requires admin privileges.
     *
     * @param lobbyId The ID of the lobby from which to remove the user.
     * @param userId Application user id.
     *
     * @return ResponseEntity containing the updated LobbyDto or an error status.
     * @throws LobbyNotFoundException if the specified lobby does not exist.
     * @throws UserNotFoundException if the user to be removed is not found in the application database.
     * @throws UserIsNotInAnyLobbyException if the specified user is not in the specified lobby.
     *      * @throws org.springframework.security.access.AccessDeniedException if the authenticated user does not have ADMIN_ROLE.
     */
    @Override
    public ResponseEntity<LobbyDto> removeUserFromLobbyAsAdmin(Integer lobbyId, Integer userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN_ROLE"))) {
            throw new org.springframework.security.access.AccessDeniedException("Access Denied: Only ADMIN_ROLE users can perform this operation.");
        }

//        Get the lobby
        Lobby lobby = lobbyFacade.readOne(lobbyId)
                .orElseThrow(() -> new LobbyNotFoundException("Lobby with ID " + lobbyId + " not found."));

//        Get user
        User userToRemove = userFacade.readOne(userId)
                .orElseThrow(() -> new UserNotFoundException("User with Keycloak ID '" + userId + "' not found in the application database."));


//        Check if users is in this specific lobby
        if (!userToRemove.hasAnyLobby() || !(userToRemove.getLobby().getId() == lobby.getId())) {
            throw new UserIsNotInAnyLobbyException("User '" + userToRemove.getLoginName() + "' (ID: " + userToRemove.getKeycloakId() + ") is not in lobby with ID " + lobbyId + ".");
        }

//        Remove user from the lobby
        lobby.removeUser(userToRemove);
//        Persist changes
        lobbyFacade.update(lobby);

//        Explicitly set user lobby to null
        userToRemove.setLobby(null);
//        Persist changes
        userFacade.update(userToRemove);

        return ResponseEntity.ok(lobbyMapper.toDto(lobby));
    }

//    Make transaction based on winner of the game
//    User sends money to winner
    @Override
    public ResponseEntity<Void> getResultOfCs2Duel(DuelResultPayloadDto duelResultPayloadDto) {
         String winner = duelResultPayloadDto.getWinner();
        String loser = duelResultPayloadDto.getLoser();

//        Get loser
        User loserUser = userFacade.readUserBySteamUsername(loser).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

//        Get winner
        User winnerUser = userFacade.readUserBySteamUsername(winner).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

//        Check if users are in same lobby
        if (loserUser.getLobby().getId() == winnerUser.getLobby().getId()) {
            creditFacade.transferCredits(
                    loserUser.getKeycloakId(),
                    winnerUser.getId(),
                    loserUser.getLobby().getAmount(),
                    "Transaction after game match"
            );
        }
//

        Lobby lobby = winnerUser.getLobby();
        lobby.setLobbyWinnerUsername(winnerUser.getLoginName());
        lobbyFacade.update(lobby);

//        Set wincount
        loserUser.setGamesPlayed(loserUser.getGamesPlayed()+1);
        loserUser.setGamesLost(loserUser.getGamesLost()+1);

        winnerUser.setGamesPlayed(winnerUser.getGamesPlayed()+1);
        winnerUser.setGamesWon(winnerUser.getGamesWon()+1);

        userFacade.update(winnerUser);
        userFacade.update(loserUser);

        CreditTransaction transactionFromLoser = new CreditTransaction(
                loserUser,
                -loserUser.getLobby().getAmount(),
                "Transaction after game match",
                winnerUser,
                TransactionType.MATCH_LOSS
        );

        CreditTransaction transactionFromWinner = new CreditTransaction(
                winnerUser,
                winnerUser.getLobby().getAmount(),
                "Transaction after game match",
                loserUser,
                TransactionType.MATCH_WIN
        );

        creditFacade.create(transactionFromLoser);
        creditFacade.create(transactionFromWinner);

        return null;
    }


//    Endpoint to send winner to the frontend
    @Override
    public ResponseEntity<WinnerResponseDto> getWinnerOfCs2Duel(Integer lobbyId) {
        Lobby lobby = lobbyFacade.readOne(lobbyId).orElseThrow(
                () -> new LobbyNotFoundException("Lobby with ID " + lobbyId + " not found.")
        );

        WinnerResponseDto winnerResponseDto = new WinnerResponseDto();
        winnerResponseDto.setWinnerUsername(lobby.getLobbyWinnerUsername());

        return ResponseEntity.ok(winnerResponseDto);
    }
}
