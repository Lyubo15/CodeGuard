package com.tu.codeguard.exceptions;

import static com.tu.codeguard.exceptions.handler.ErrorCode.ENTITY_NOT_FOUND;

public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException(String message) {
        super(message, ENTITY_NOT_FOUND);
    }
}
