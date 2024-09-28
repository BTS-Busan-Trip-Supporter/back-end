package org.bts.backend.exception.after_servlet;

import org.bts.backend.exception.CustomException;
import org.springframework.http.HttpStatus;

public class MailException extends CustomException {
    public MailException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송에 실패했습니다.");
    }
}
