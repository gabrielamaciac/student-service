package com.learning.student.studentservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class IllegalStateExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ResponseEntity<Object> handleConflict(final IllegalStateException ex, final WebRequest request) {
        log.error("IllegalStateExceptionHandler caught exception: " + ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}