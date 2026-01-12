package com.example.demo.modules.Race.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.modules.Race.application.web.dto.RouteRequestDTO;

@Service
public class PriceService {

    private static final Double PRICE_PER_KM = 2.0;
    private static final Double BASE_FARE = 5.0;

    @Autowired
    private DistanceService distanceService;

    public Double calculatePrice(RouteRequestDTO routeRequest) {
        Double distanceKm = distanceService.calculate(routeRequest).distanceKm();
        return BASE_FARE + (distanceKm * PRICE_PER_KM);
    }
}
