package org.windat.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) //400
public class UserIsNotInAnyLobbyException extends RuntimeException {
    public UserIsNotInAnyLobbyException(String message) {
        super(message);
    }
}
