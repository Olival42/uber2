package com.example.demo.infrastructure.exception;

public class RouteNotFoundException extends RuntimeException {

    public RouteNotFoundException() {
        super("Error calculating distance: No route found");
    }
}