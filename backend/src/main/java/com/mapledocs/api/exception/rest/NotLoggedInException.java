package com.mapledocs.api.exception.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotLoggedInException extends RuntimeException {
    public NotLoggedInException(String msg) {
        super(msg);
    }
}
