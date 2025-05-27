package org.windat.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an operation requires a certain amount of credits,
 * but the user's current balance is insufficient.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST) // Повертає 400 Bad Request, якщо виняток не перехоплено явно
public class InsufficientCreditsException extends RuntimeException {

    /**
     * Constructs a new InsufficientCreditsException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public InsufficientCreditsException(String message) {
        super(message);
    }

    /**
     * Constructs a new InsufficientCreditsException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).
     */
    public InsufficientCreditsException(String message, Throwable cause) {
        super(message, cause);
    }
}