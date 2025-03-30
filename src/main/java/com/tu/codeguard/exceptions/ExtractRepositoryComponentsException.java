package com.tu.codeguard.exceptions;

import com.tu.codeguard.exceptions.handler.ErrorCode;

public class ExtractRepositoryComponentsException extends BaseException {

    public ExtractRepositoryComponentsException(String message) {
        super(message, ErrorCode.EXTRACT_REPOSITORY_COMPONENTS_FAILED);
    }
}
