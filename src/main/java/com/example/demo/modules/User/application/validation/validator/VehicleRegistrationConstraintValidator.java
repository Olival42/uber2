package com.example.demo.modules.User.application.validation.validator;


import java.util.regex.Pattern;

import com.example.demo.modules.User.application.validation.anotation.ValidVehicleRegistration;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VehicleRegistrationConstraintValidator implements ConstraintValidator<ValidVehicleRegistration, String>{

     private static final Pattern VEHICLE_REGISTRATION_PATTERN = Pattern.compile(
            "^[A-Z]{3}\\d[A-Z]\\d{2}$",
            Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return VEHICLE_REGISTRATION_PATTERN.matcher(value).matches();
    }
}
