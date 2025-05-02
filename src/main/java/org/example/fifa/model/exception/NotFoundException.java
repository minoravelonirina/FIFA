package org.example.fifa.model.exception;


import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException {

    public NotFoundException(HttpStatus status, String message) {
        super(message);
    }
}
