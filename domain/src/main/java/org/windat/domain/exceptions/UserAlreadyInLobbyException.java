package org.windat.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409
public class UserAlreadyInLobbyException extends RuntimeException {
    public UserAlreadyInLobbyException(String message) {
        super(message);
    }
}
