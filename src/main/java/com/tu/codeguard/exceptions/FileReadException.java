package com.tu.codeguard.exceptions;

import com.tu.codeguard.exceptions.handler.ErrorCode;

public class FileReadException extends BaseException {

    public FileReadException(String message) {
        super(message, ErrorCode.FILE_READ_FAILED);
    }
}
