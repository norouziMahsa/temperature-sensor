package com.qardio.assignment.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class ApplicationError {
    private final String message;
    private final HttpStatus status;
}
