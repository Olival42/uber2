package com.example.demo.infrastructure.exception.security;

public class InvalidTokenTypeException extends RuntimeException {
    public InvalidTokenTypeException(String message) {
        super(message);
    }

}
