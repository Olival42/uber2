package com.example.demo.modules.User.application.validation.validator;

import com.example.demo.modules.User.application.validation.anotation.ValidPhone;
import com.example.demo.shared.validation.validator.PhoneValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneConstraintValidator implements ConstraintValidator<ValidPhone, String>{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return PhoneValidator.isValid(value);
    }
}
