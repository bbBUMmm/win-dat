package org.windat.domain.entity;

import org.windat.domain.UserRole;

import java.util.UUID;

/**
 * Represents a user within the application.
 * This class encapsulates the state and behavior of a user, including their unique
 * identifier, login name, role, and game lobby they are currently associated with.
 */
public class User {

    /**
     * Unique identifier for the user.
     * Used as the primary key in the database.
     */
    private int id;

    /**
     * Unique keycloak identifier
     * Used to find specific user in keycloak database
     */
    private UUID keycloakId;

    /**
     * The login name of the user, used for authentication and identification.
     */
    private String loginName;

    /**
     * The role of the user within the application, represented by an enumeration.
     * Defines the user's permissions and access levels.
     */
    private UserRole userRoleEnum;

    /**
     * The game lobby the user is currently associated with.
     * Represents the game session the user is participating in.
     * Can be null if the user is not in a lobby.
     */
    private Lobby lobby;

    /**
     * Default constructor for Hibernate to map entities.
     * Required for JPA entity instantiation.
     */
    public User() {

    }

    /**
     * Retrieves the unique identifier of the user.
     *
     * @return The user's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the login name of the user.
     *
     * @return The user's login name.
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * Retrieves the role of the user.
     *
     * @return The user's role as an enumeration.
     */
    public UserRole getUserRoleEnum() {
        return userRoleEnum;
    }

    /**
     * Retrieves the lobby the user is currently associated with.
     *
     * @return The Lobby object, or null if the user is not in a lobby.
     */
    public Lobby getLobby() {
        return lobby;
    }

    /**
     * Gets id of the lobby user is currently in
     * @return lobby id
     */
    public Integer getCurrentLobbyId(){
        return (this.lobby != null) ? this.lobby.getId() : null;
    }


    /**
     *
     */
    public UUID getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(UUID keycloakId) {
        this.keycloakId = keycloakId;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setUserRoleEnum(UserRole userRoleEnum) {
        this.userRoleEnum = userRoleEnum;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public boolean hasAnyLobby(){
        return this.lobby != null;
    }
}