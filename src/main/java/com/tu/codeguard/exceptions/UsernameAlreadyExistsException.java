package com.tu.codeguard.exceptions;

import static com.tu.codeguard.exceptions.handler.ErrorCode.ENTITY_ALREADY_EXISTS;

public class UsernameAlreadyExistsException extends BaseException {
    public UsernameAlreadyExistsException() {
        super("Username already exists", ENTITY_ALREADY_EXISTS);
    }
}
