package com.example.demo.modules.User.application.web.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class DriverResponseDTO extends UserResponseDTO {

    private String cnh;
    private String vehicleRegistration;
    private String modelVehicle;
    private String yearVehicle;
    private String colorVehicle;
}
