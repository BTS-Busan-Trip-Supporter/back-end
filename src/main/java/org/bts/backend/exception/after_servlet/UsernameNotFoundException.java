package org.bts.backend.exception.after_servlet;

import org.bts.backend.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UsernameNotFoundException extends CustomException {
    public UsernameNotFoundException() {
        super(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다.");
    }
    public UsernameNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
