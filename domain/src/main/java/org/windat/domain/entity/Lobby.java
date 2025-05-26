package org.windat.domain.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a game lobby where users can join and participate in a game session.
 * This class encapsulates the state and behavior of a lobby, including its unique
 * identifier, name, list of users, and timestamps for creation, update, and closure.
 */
public class Lobby {

    /**
     * Default constructor for Hibernate to map entities.
     * Required for JPA entity instantiation.
     */
    public Lobby(){
        this.userList = new ArrayList<>();
    }


    /**
     * Unique identifier for the lobby.
     * Used as the primary key in the database.
     */
    private int id;

    /**
     * The name of the lobby, providing a human-readable identifier.
     * Helps users easily recognize and join specific lobbies.
     */
    private String name;

    /**
     * Timestamp indicating when the lobby was created.
     * Useful for tracking lobby creation time and potential cleanup of old lobbies.
     */
    private Date created;

    /**
     * Timestamp indicating the last time the lobby was updated.
     * Can be used to track changes to the lobby's state or metadata.
     */
    private Date updated;

    /**
     * Timestamp indicating when the lobby was closed or terminated.
     * Helps manage the lobby lifecycle and identify completed or inactive sessions.
     */
    private Date closed;

    /**
     * List of users currently participating in the lobby.
     * Represents the active users within the game session.
     * Note: the list is limited to 2 users in one lobby
     */
    private List<User> userList;

    /**
     * Adds a user to the lobby's user list.
     *
     * @param user The User object to add to the lobby.
     */
    public void addUser(User user){
        if (user == null) {
            throw new NullPointerException("User cannot be null");
        }
        userList.add(user);
        user.setLobby(this);
    }

    /**
     * Removes a user from the lobby's user list.
     *
     * @param user The User object to remove from the lobby.
     */
    public void removeUser(User user){
        if(user == null){
            throw new IllegalArgumentException("User cannot be null");
        }
        userList.remove(user);
        user.setLobby(null);
    }

    /**
     * Checks if the lobby is full based on the maximum number of users allowed.
     *
     * @return true if the lobby is full, false otherwise.
     * Note: The logic `userList.size() > 2` indicates that the lobby can hold a maximum of 2 users.
     */
    public boolean isFull(){
        return this.userList.size() >= 2;
    }

    /**
     * Checks if lobby already has specific user
     *
     * @return boolean
     */
    public boolean containsUser(User user){
        return userList.contains(user);
    }

    /*
     * Getters for the class attributes
     */

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUserList() {
        return userList;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public void setClosed(Date closed) {
        this.closed = closed;
    }

    public Date getUpdated() {
        return updated;
    }

    public Date getClosed() {
        return closed;
    }
}