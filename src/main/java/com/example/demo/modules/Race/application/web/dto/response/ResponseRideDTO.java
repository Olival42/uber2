package com.example.demo.modules.Race.application.web.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.example.demo.modules.Race.domain.enums.StatusRide;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseRideDTO {

    private UUID rideId;
    private Double distanceKm;
    private Integer durationMinutes;
    private Double price;
    private UUID passengerId;
    private StatusRide status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss", timezone = "America/Sao_Paulo")
    private Instant requestedAt;
    private UUID driverId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss", timezone = "America/Sao_Paulo")
    private Instant acceptedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss", timezone = "America/Sao_Paulo")
    private Instant startedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss", timezone = "America/Sao_Paulo")
    private Instant finishedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss", timezone = "America/Sao_Paulo")
    private Instant cancelledAt;
    private String cancellationReason;
}
