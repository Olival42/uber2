package com.example.demo.infrastructure.exception.security;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
