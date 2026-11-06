package com.confsync.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when conference maximum participant limit is reached.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ConferenceCapacityException extends RuntimeException {
    public ConferenceCapacityException(String message) {
        super(message);
    }
}

