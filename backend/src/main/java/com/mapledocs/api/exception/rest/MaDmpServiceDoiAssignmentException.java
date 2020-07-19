package com.mapledocs.api.exception.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class MaDmpServiceDoiAssignmentException extends RuntimeException {
    public MaDmpServiceDoiAssignmentException(String s) {
    }
}
