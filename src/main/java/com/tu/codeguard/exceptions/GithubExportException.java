package com.tu.codeguard.exceptions;

import com.tu.codeguard.exceptions.handler.ErrorCode;

public class GithubExportException extends BaseException {

    public GithubExportException(String message) {
        super(message, ErrorCode.GITHUB_API_ERROR);
    }
}
