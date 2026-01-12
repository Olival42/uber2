package com.example.demo.modules.Race.infrastructure.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.infrastructure.exception.RouteNotFoundException;
import com.example.demo.modules.Race.application.web.dto.RouteRequestDTO;
import com.example.demo.modules.Race.application.web.dto.RouteResponseDTO;

@Component
public class OpenRouteClient {

    @Autowired
    private WebClient webClient;

    @Value("${openroute.api.key}")
    private String apiKey;

    public RouteResponseDTO fetchRoute(RouteRequestDTO coordinates) {

        String url = "https://api.openrouteservice.org/v2/directions/driving-car";

        RouteResponseDTO response = webClient
                .post()
                .uri(url)
                .header("Authorization", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(coordinates)
                .retrieve()
                .bodyToMono(RouteResponseDTO.class)
                .block();

        if (response == null || response.getRoutes() == null || response.getRoutes().isEmpty()) {
            throw new RouteNotFoundException();
        }

        return response;
    }
}
