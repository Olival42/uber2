package com.example.demo.modules.Race.application.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAddressRequestDTO {

    @NotBlank(message = "Street is required")
    private String street;

    @Pattern(regexp = "\\d+", message = "Number must be numeric")
    private String number;

    @NotBlank(message = "Neighborhood is required")
    private String neighborhood;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "\\d{8}", message = "Postal code must be in the format 12345678")
    private String postalCode;
}
