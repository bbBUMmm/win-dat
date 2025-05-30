package org.windat.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED) // 412
public class UserDoesNotHaveEnoughBalanceException extends RuntimeException {
    public UserDoesNotHaveEnoughBalanceException(String message) {
        super(message);
    }
}
