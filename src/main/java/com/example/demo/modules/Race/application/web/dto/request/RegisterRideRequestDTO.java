package com.example.demo.modules.Race.application.web.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRideRequestDTO {

    @NotNull(message = "Passenger ID is required")
    private UUID passengerId;

    @NotNull(message = "Pickup address is required")
    private RegisterAddressRequestDTO pickupAddress;

    @NotNull(message = "Destination address is required")
    private RegisterAddressRequestDTO destinationAddress;
}
