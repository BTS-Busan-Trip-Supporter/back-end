package org.bts.backend.exception.after_servlet;

import org.bts.backend.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NoTourSpotsFoundException extends CustomException {
    public NoTourSpotsFoundException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "추천할 관광지가 없습니다.");
    }
}
