package org.windat.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyHasLobbyWithNameException extends RuntimeException {
  public UserAlreadyHasLobbyWithNameException(String message) {
    super(message);
  }
}
