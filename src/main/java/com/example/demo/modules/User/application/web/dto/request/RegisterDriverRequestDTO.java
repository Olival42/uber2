package com.example.demo.modules.User.application.web.dto.request;

import org.hibernate.validator.constraints.Length;

import com.example.demo.modules.User.application.validation.anotation.ValidCnh;
import com.example.demo.modules.User.application.validation.anotation.ValidPassword;
import com.example.demo.modules.User.application.validation.anotation.ValidVehicleRegistration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterDriverRequestDTO {

    @NotBlank(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @Length(min = 8, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Password cannot be null")
    @ValidPassword(message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;

    @NotBlank(message = "Name cannot be null")
    private String name;

    @NotBlank(message = "CNH cannot be null")
    @Length(min = 11, message = "CNH must be at least 11 characters long")
    @ValidCnh(message = "CNH invalid")
    private String cnh;

    @NotBlank(message = "Register of vehicle cannot be null")
    @Length(min = 7, message = "Register of vehicle must be at least 7 characters long")
    @ValidVehicleRegistration(message = "Register of vehicle invalid")
    private String vehicleRegistration;

    @NotBlank(message = "Model of vehicle cannot be null")
    private String modelVehicle;

    @Length(min = 4, message = "Year of vehicle must be at least 4 characters long")
    @NotBlank(message = "Year of vehicle cannot be null")
    @Pattern(regexp = "^\\d{4}$", message = "Year of vehicle invalid")
    private String yearVehicle;

    @NotBlank(message = "Color of vehicle cannot be null")
    private String colorVehicle;
}
