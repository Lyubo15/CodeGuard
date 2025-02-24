package com.tu.codeguard.exceptions.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * REST API error object containing main data for the error and additional developer friendly information.
 */
@Getter
@JsonPropertyOrder({"code", "exceptionId", "timestamp", "errorCode", "message", "userMessage"})
public class ApiError {

    /**
     * Unified code of the error. Also used for userMessage.
     */
    private ErrorCode errorCode;

    /**
     * Developer-friendly message in English.
     */
    private String message;

    /**
     * Translated message.
     */
    private String userMessage;

    /**
     * Additional developer-friendly error details to aid debugging.
     */
    @JsonInclude(Include.NON_NULL)
    private String[] errors;

    /**
     * Uuid for easier correlation of the error.
     */
    private final String exceptionId;

    /**
     * Timestamp of the error.
     */
    private final LocalDateTime timestamp;

    private ApiError() {
        timestamp = LocalDateTime.now();
        exceptionId = UUID
                .randomUUID().toString().replace("-", "");
    }

    public ApiError(ErrorCode errorCode, String message, String userMessage, String... errorDetails) {
        this();
        this.errorCode = errorCode;
        this.message = message;
        this.userMessage = userMessage;
        this.errors = errorDetails;
    }
}