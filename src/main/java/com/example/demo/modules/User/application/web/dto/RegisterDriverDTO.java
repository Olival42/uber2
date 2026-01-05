package com.example.demo.modules.User.application.web.dto;

import org.hibernate.validator.constraints.Length;

import com.example.demo.modules.User.application.validation.anotation.ValidCnh;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterDriverDTO {

    @NotBlank(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @Length(min = 8, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Password cannot be null")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;

    @NotBlank(message = "Name cannot be null")
    private String name;

    @NotBlank(message = "CNH cannot be null")
    @Length(min = 11, message = "CNH must be at least 11 characters long")
    @ValidCnh
    private String cnh;

    @NotBlank(message = "Register of vehicle cannot be null")
    @Length(min = 7, message = "Register of vehicle must be at least 7 characters long")
    @Pattern(regexp = "^[A-Z]{3}\\d[A-Z]\\d{2}$", message = "Register of vehicle invalid")
    private String vehicleRegistration;

    @NotBlank(message = "Model of vehicle cannot be null")
    private String modelVehicle;

    @Length(min = 4, message = "Year of vehicle must be at least 4 characters long")
    @NotBlank(message = "Year of vehicle cannot be null")
    private String yearVehicle;

    @NotBlank(message = "Color of vehicle cannot be null")
    private String colorVehicle;
}
