package com.example.demo.modules.Race.adapter.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.modules.Race.application.web.dto.DistanceResult;
import com.example.demo.modules.Race.application.web.dto.response.RouteResponseDTO;

@Component
public class RouteMapper {

    public DistanceResult toDistanceResult(RouteResponseDTO response) {

        var summary = response.getRoutes().get(0).getSummary();

        return new DistanceResult(
                summary.getDistance(),
                summary.getDuration()
        );
    }
}
