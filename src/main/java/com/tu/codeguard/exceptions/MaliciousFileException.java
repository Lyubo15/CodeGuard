package com.tu.codeguard.exceptions;

import static com.tu.codeguard.exceptions.handler.ErrorCode.MALICIOUS_FILE_DETECTED;

public class MaliciousFileException extends BaseException {
    public MaliciousFileException(String message) {
        super(message, MALICIOUS_FILE_DETECTED);
    }
}
