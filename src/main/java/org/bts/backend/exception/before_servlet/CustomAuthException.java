package org.bts.backend.exception.before_servlet;

import org.bts.backend.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CustomAuthException extends CustomException {
    public CustomAuthException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "인증 과정에서 문제가 발생했습니다.");
    }
    public CustomAuthException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
