package com.example.demo.infrastructure.exception;

public class GeocodingNotFoundException extends RuntimeException {
    public GeocodingNotFoundException(String message) {
        super(message);
    }

}
