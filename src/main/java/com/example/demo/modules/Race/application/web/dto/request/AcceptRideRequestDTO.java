package com.example.demo.modules.Race.application.web.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcceptRideRequestDTO {

    @NotNull(message = "Driver ID is required")
    private UUID driverId;
}
