package org.bts.backend.exception.after_servlet;

import org.bts.backend.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NoCredException extends CustomException {
    public NoCredException() {
        super(HttpStatus.UNAUTHORIZED, "인증 정보가 없습니다.");
    }
}
