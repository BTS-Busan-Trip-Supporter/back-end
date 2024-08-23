package org.bts.backend.exception.before_servlet;

import org.bts.backend.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CustomIOException extends CustomException {
    public CustomIOException() {
        super(HttpStatus.BAD_REQUEST, "IO Exception");
    }
    public CustomIOException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
