package com.example.demo.infrastructure.exception.security;

public class MissingTokenException extends RuntimeException {
    public MissingTokenException(String message) {
        super(message);
    }

}
