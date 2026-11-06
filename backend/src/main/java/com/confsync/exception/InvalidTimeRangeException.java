package com.confsync.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when session or conference start/end times are logically inverted.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTimeRangeException extends RuntimeException {
    public InvalidTimeRangeException(String message) {
        super(message);
    }
}

