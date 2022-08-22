package com.qardio.assignment.handler;

import com.qardio.assignment.exception.ApplicationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApplicationError> handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        ApplicationError error = ApplicationError
                .builder()
                .message(exception.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }
}
