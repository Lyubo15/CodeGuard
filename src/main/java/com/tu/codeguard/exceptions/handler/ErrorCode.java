package com.tu.codeguard.exceptions.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Enum with a unified code of the error if FE wants to have specific handling. Each value, has corresponding properties
 * in the message.properties files for each supported language.
 * <p>
 * Keep sorted for easier maintenance! Properties files, too.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ACCESS_DENIED(UNAUTHORIZED),
    CONSTRAINT_VIOLATION(BAD_REQUEST),
    CONTROLLER_NAMING_VIOLATION(INTERNAL_SERVER_ERROR),
    DEFAULT_ERROR(INTERNAL_SERVER_ERROR),
    ENTITY_NOT_FOUND(NOT_FOUND),
    ILLEGAL_ARGUMENT(BAD_REQUEST),
    INVALID_PARAMETER(BAD_REQUEST),
    MISSING_PARAMETER(BAD_REQUEST),
    PRIMARY_KEY_VIOLATION(BAD_REQUEST),
    QUEUE_MESSAGE_ERROR(BAD_REQUEST),
    TYPE_MISMATCH(BAD_REQUEST),
    UNAUTHENTICATED(FORBIDDEN),
    VALIDATION_FAILURE(BAD_REQUEST),
    INVALID_DATE(BAD_REQUEST),
    DATABASE_INTEGRITY_VIOLATION(INTERNAL_SERVER_ERROR),
    FILE_READ_FAILED(INTERNAL_SERVER_ERROR),
    MALICIOUS_FILE_DETECTED(BAD_REQUEST);

    /**
     * HTTP status associated with this error code.
     */
    private final HttpStatus httpStatus;
}
