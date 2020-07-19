package com.mapledocs.api.exception.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MaDmpServiceValidationException extends RuntimeException {
    public MaDmpServiceValidationException(String s) {
    }
}
