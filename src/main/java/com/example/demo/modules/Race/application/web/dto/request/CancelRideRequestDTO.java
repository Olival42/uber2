package com.example.demo.modules.Race.application.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelRideRequestDTO {

    @NotBlank(message = "Cancellation reason is required")
    private String cancellationReason;
}
