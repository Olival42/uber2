package com.example.demo.modules.Race.infrastructure.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.infrastructure.exception.GeocodingNotFoundException;
import com.example.demo.modules.Race.application.web.dto.response.CoordinateResponseDTO;
import com.example.demo.modules.Race.domain.entity.AddressEntity;
import com.example.demo.modules.Race.domain.service.util.AddressFormatter;

@Service
public class NominatimClient {

    @Autowired
    private WebClient webClient;

    public CoordinateResponseDTO fetchCoordinates(AddressEntity address) {

        CoordinateResponseDTO[] response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("nominatim.openstreetmap.org")
                        .path("/search")
                        .queryParam("q", AddressFormatter.buildFullAddress(address))
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .build())
                .header("User-Agent", "RideApp/1.0")
                .retrieve()
                .bodyToMono(CoordinateResponseDTO[].class)
                .block();


        if (response == null || response.length == 0) {
            throw new GeocodingNotFoundException("Error fetching coordinates: No results found for address");
        }

        return response[0];
    }

    
}
