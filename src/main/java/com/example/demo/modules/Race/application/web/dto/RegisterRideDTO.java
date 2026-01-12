package com.example.demo.modules.Race.application.web.dto;

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
public class RegisterRideDTO {

    @NotNull(message = "Passenger ID is required")
    private UUID passengerId;

    @NotNull(message = "Pickup address is required")
    private RegisterAddressDTO pickupAddress;

    @NotNull(message = "Destination address is required")
    private RegisterAddressDTO destinationAddress;
}
