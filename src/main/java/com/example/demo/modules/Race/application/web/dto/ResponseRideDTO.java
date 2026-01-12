package com.example.demo.modules.Race.application.web.dto;

import java.time.Instant;
import java.util.UUID;

import com.example.demo.modules.Race.domain.enums.StatusRide;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseRideDTO {

    private UUID rideId;
    private Double distanceKm;
    private Integer durationMinutes;
    private Double price;
    private UUID passengerId;
    private StatusRide status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss", timezone = "America/Sao_Paulo")
    private Instant requestedAt;

}
