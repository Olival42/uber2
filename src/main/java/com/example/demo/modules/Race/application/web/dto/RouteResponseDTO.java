package com.example.demo.modules.Race.application.web.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponseDTO {

    private List<Route> routes;

    @Data
    public static class Route {
        private Summary summary;
    }

    @Data
    public static class Summary {
        private double distance;
        
        private double duration;
    }
}
