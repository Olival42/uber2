package com.example.demo.infrastructure.exception.security;

public class InsufficientAuthenticationException extends RuntimeException {
    public InsufficientAuthenticationException(String message) {
        super(message);
    }

    public InsufficientAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

}
