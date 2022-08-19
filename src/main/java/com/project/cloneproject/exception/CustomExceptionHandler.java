package com.project.cloneproject.exception;

import com.project.cloneproject.response.ResponseDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseDto<?> illegalArgumentException(IllegalArgumentException exception) {
        return ResponseDto.fail("BAD_REQUEST", exception.getMessage());
    }
}
