package org.bts.backend.exception;

import lombok.Getter;
import org.bts.backend.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final ApiResponse<String> response;

    public CustomException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.response = ApiResponse.fail(message);
    }
}
