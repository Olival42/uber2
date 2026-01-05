package com.example.demo.infrastructure.exception.security;

public class TokenRevokedException extends RuntimeException {
    public TokenRevokedException(String message) {
        super(message);
    }
}
