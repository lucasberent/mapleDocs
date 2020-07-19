package com.mapledocs.api.exception.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class MaDmpServiceCreationException extends RuntimeException {
    public MaDmpServiceCreationException(String s) {
        super(s);
    }

    public MaDmpServiceCreationException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
