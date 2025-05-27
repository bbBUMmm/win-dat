package org.windat.domain.entity;

import org.windat.domain.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a single credit transaction within the WinDat platform.
 * This entity records the details of any credit change for a user,
 * providing a comprehensive history of how credits are gained or lost.
 */
public class CreditTransaction {

    /**
     * Unique identifier for the credit transaction.
     * Serves as the primary key in the database.
     */
    private Integer id;

    /**
     * The {@link User} entity who is the primary participant (receiver or giver)
     * of this credit transaction.
     */
    private User user;

    /**
     * The amount of credits involved in this transaction.
     * Can be positive (for gains) or negative (for losses).
     */
    private Integer amount;

    /**
     * The date and time when this transaction occurred.
     */
    private LocalDateTime transactionTime;

    /**
     * A descriptive text explaining the nature of the transaction.
     * Examples: "Win from match", "Loss in match", "Starting bonus", "Monthly bonus", "Admin adjustment".
     */
    private String description;

    /**
     * A reference to another {@link User} entity involved in the transaction, if applicable.
     * For example, in a match, this could be the opponent. Can be {@code null}.
     */
    private User relatedUser;

    /**
     * The specific type of the transaction, represented by a {@link TransactionType} enumeration.
     * Defines the category of the credit change (e.g., MATCH_WIN, MATCH_LOSS, INITIAL_BONUS).
     */
    private TransactionType type;

    /**
     * Default constructor for Hibernate (JPA) to map entities.
     * Initializes the {@code transactionTime} to the current {@link LocalDateTime}.
     */
    public CreditTransaction() {
        this.transactionTime = LocalDateTime.now();
    }

    /**
     * Constructs a new CreditTransaction with specified details.
     *
     * @param user          The primary user involved in the transaction (receiver/giver). Must not be null.
     * @param amount        The amount of credits for this transaction. Can be positive or negative. Must not be null.
     * @param description   A descriptive text for the transaction.
     * @param relatedUser   The secondary user involved in the transaction (e.g., opponent). Can be null.
     * @param type          The type of the transaction. Must not be null.
     * @throws NullPointerException     if user, amount, or type is null.
     * @throws IllegalArgumentException if description is null or empty.
     */
    public CreditTransaction(User user, Integer amount, String description, User relatedUser, TransactionType type) {
        this.user = Objects.requireNonNull(user, "User cannot be null.");
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null.");
//        Description can be null
        this.description = description;
//        relatedUser can be null
        this.relatedUser = relatedUser;
        this.type = Objects.requireNonNull(type, "Transaction type cannot be null.");
//        Set current transactionTime
        this.transactionTime = LocalDateTime.now();
    }

    /**
     * Retrieves the unique identifier of the credit transaction.
     *
     * @return The ID of the transaction.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Retrieves the primary user involved in this transaction.
     *
     * @return The {@link User} entity associated with this transaction.
     */
    public User getUser() {
        return user;
    }

    /**
     * Retrieves the amount of credits involved in this transaction.
     *
     * @return The credit amount (positive for gains, negative for losses).
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Retrieves the date and time when this transaction occurred.
     *
     * @return The {@link LocalDateTime} of the transaction.
     */
    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    /**
     * Retrieves the description of the transaction.
     *
     * @return A string describing the transaction.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the secondary user involved in the transaction, if any.
     *
     * @return The {@link User} entity who is the related participant, or {@code null}.
     */
    public User getRelatedUser() {
        return relatedUser;
    }

    /**
     * Retrieves the type of this credit transaction.
     *
     * @return The {@link TransactionType} of the transaction.
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * Sets the primary user for this transaction.
     *
     * @param user The {@link User} entity to set. Must not be null.
     * @throws NullPointerException if the provided user is null.
     */
    public void setUser(User user) {
        this.user = Objects.requireNonNull(user, "User cannot be null.");
    }

    /**
     * Sets the amount of credits for this transaction.
     *
     * @param amount The credit amount to set. Must not be null.
     * @throws NullPointerException if the provided amount is null.
     */
    public void setAmount(Integer amount) {
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null.");
    }

    /**
     * Sets the date and time for this transaction.
     *
     * @param transactionTime The {@link LocalDateTime} to set. Must not be null.
     * @throws NullPointerException if the provided transactionTime is null.
     */
    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = Objects.requireNonNull(transactionTime, "Transaction time cannot be null.");
    }

    /**
     * Sets the description for this transaction.
     *
     * @param description The description string to set. Must not be null or empty.
     * @throws IllegalArgumentException if the provided description is null or empty.
     */
    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        this.description = description;
    }

    /**
     * Sets the secondary user involved in the transaction.
     *
     * @param relatedUser The {@link User} entity to set as the related participant. Can be {@code null}.
     */
    public void setRelatedUser(User relatedUser) {
        this.relatedUser = relatedUser;
    }

    /**
     * Sets the type of this credit transaction.
     *
     * @param type The {@link TransactionType} to set. Must not be null.
     * @throws NullPointerException if the provided type is null.
     */
    public void setType(TransactionType type) {
        this.type = Objects.requireNonNull(type, "Transaction type cannot be null.");
    }
}