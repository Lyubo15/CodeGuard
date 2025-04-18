package com.tu.codeguard.exceptions;

public class UsernameAlreadyExistsException extends BaseException {
    public UsernameAlreadyExistsException() {
        super("Username already exists");
    }
}
