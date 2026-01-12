package com.example.demo.infrastructure;

public class GeocodingNotFoundException extends RuntimeException {
    public GeocodingNotFoundException(String message) {
        super(message);
    }

}
