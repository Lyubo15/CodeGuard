package com.tu.codeguard.exceptions;

import com.tu.codeguard.exceptions.handler.ErrorCode;
import lombok.Getter;

import java.io.Serializable;

/**
 * Generic exception for backend errors. Subclass it, if you want to customize it for a specific need.
 */
@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Serializable[] errorCodeParameters;

    /**
     * Create an exception with a specific message wrapping the original exception.
     */
    public BaseException(String message, Throwable throwable, ErrorCode errorCode,
                         Serializable... errorCodeParameters) {
        super(message, throwable);
        this.errorCode = errorCode;
        this.errorCodeParameters = errorCodeParameters;
    }

    /**
     * Create an exception with a specific message.
     */
    public BaseException(String message, ErrorCode errorCode, Serializable... errorCodeParameters) {
        this(message, null, errorCode, errorCodeParameters);
    }

    /**
     * Create a default exception wrapping the original exception.
     */
    @SuppressWarnings("unused")
    public BaseException(Throwable throwable) {
        this(throwable.getMessage(), throwable, ErrorCode.DEFAULT_ERROR);
    }

    /**
     * Create a default exception with specific error details.
     */
    @SuppressWarnings("unused")
    public BaseException(String message) {
        this(message, ErrorCode.DEFAULT_ERROR);
    }

}
