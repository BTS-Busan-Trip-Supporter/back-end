package org.bts.backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MVCGlobalException {

    // CustomException을 처리하는 핸들러 -> RuntimeException을 상속받아서 사용
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customExceptionHandler(CustomException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getResponse());
    }
}
