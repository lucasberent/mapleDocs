package com.mapledocs.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class MaDmpServiceCreationException extends RuntimeException {
    public MaDmpServiceCreationException(String s) {
        super(s);
    }
}
