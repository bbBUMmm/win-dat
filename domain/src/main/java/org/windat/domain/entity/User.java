package org.windat.domain.entity;

import org.windat.domain.enums.UserRole;
import org.windat.domain.exceptions.InsufficientCreditsException;

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
     * Represents user balance in the WinDat platform
     */
    private Integer credits;

    private Integer gamesPlayed;

    private Integer gamesWon;

    private Integer gamesLost;
    /**
     * Default constructor for Hibernate to map entities.
     * Initializes a new User instance, setting the initial credit balance to 10,000.
     * Required for JPA entity instantiation.
     */
    public User() {

        this.credits = 10000;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.gamesLost = 0;
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

    /**
     * Retrieves the current credit balance of the user.
     *
     * @return The total number of credits the user possesses.
     */
    public Integer getCredits() {
        return credits;
    }

    /**
     * Sets the credit balance for the user.
     * This method should be used cautiously, typically for initial setup or administrative adjustments.
     * For adding/deducting credits, use {@link #addCredits(Integer)} and {@link #deductCredits(Integer)}.
     *
     * @param credits The new credit balance to set. Must not be null and must be non-negative.
     * @throws IllegalArgumentException if the provided credits value is null or negative.
     */
    public void setCredits(Integer credits) {
        if (credits == null || credits < 0) {
            throw new IllegalArgumentException("Credits cannot be null or negative.");
        }
        this.credits = credits;
    }

    /**
     * Adds a specified amount of credits to the user's current balance.
     *
     * @param amount The amount of credits to add. Must be a positive integer.
     * @throws IllegalArgumentException if the provided amount is negative.
     */
    public void addCredits(Integer amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount to add must be positive.");
        }
        this.credits += amount;
    }


    /**
     * Deducts a specified amount of credits from the user's current balance.
     *
     * @param amount The amount of credits to deduct. Must be a positive integer.
     * @throws IllegalArgumentException     if the provided amount is negative or null.
     * @throws InsufficientCreditsException if the user's current credit balance is less than the amount to deduct.
     */
    public void deductCredits(Integer amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount to deduct must be positive.");
        }
        if (this.credits < amount) {
            throw new InsufficientCreditsException("User has insufficient credits.");
        }
        this.credits -= amount;
    }

    public Integer getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(Integer gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public Integer getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(Integer gamesWon) {
        this.gamesWon = gamesWon;
    }

    public Integer getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(Integer gamesLost) {
        this.gamesLost = gamesLost;
    }
}