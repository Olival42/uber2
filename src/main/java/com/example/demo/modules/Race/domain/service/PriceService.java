package com.example.demo.modules.Race.domain.service;

import org.springframework.stereotype.Service;

import com.example.demo.modules.Race.application.web.dto.RouteInfo;

@Service
public class PriceService {

    private static final Double PRICE_PER_KM = 2.0;
    private static final Double BASE_FARE = 5.0;

    public Double calculatePrice(RouteInfo route) {
        Double distanceKm = route.distanceKm();
        return BASE_FARE + (distanceKm * PRICE_PER_KM);
    }
}
