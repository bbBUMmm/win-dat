package org.windat.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LobbyFullException extends RuntimeException {
  public LobbyFullException(String message) {
    super(message);
  }
}
