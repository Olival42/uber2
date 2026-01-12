package com.example.demo.modules.Race.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.modules.Race.adapter.mapper.RouteMapper;
import com.example.demo.modules.Race.application.web.dto.RouteInfo;
import com.example.demo.modules.Race.application.web.dto.RouteRequestDTO;
import com.example.demo.modules.Race.infrastructure.client.OpenRouteClient;

@Service
public class DistanceService {

    @Autowired
    private OpenRouteClient routeClient;

    @Autowired
    private RouteMapper routeMapper;

    public RouteInfo calculate(RouteRequestDTO coordinates) {
        var routeResponse = routeClient.fetchRoute(coordinates);
        var distanceResult = routeMapper.toDistanceResult(routeResponse);
        return new RouteInfo(distanceResult.distance() / 1000.0, (int) Math.ceil(distanceResult.duration() / 60.0));
    }
}