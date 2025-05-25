package org.windat.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LobbyNotFoundException extends RuntimeException {
    public LobbyNotFoundException(String message) {
        super(message);
    }
}
