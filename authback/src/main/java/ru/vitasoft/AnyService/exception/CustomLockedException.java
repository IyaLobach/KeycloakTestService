package ru.vitasoft.AnyService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class CustomLockedException extends RuntimeException {

    public CustomLockedException(String message) {
    super(message);
    }
}