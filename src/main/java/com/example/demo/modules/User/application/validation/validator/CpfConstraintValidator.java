package com.example.demo.modules.User.application.validation.validator;

import com.example.demo.modules.User.application.validation.anotation.ValidCpf;
import com.example.demo.shared.validation.validator.CPFValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfConstraintValidator implements ConstraintValidator<ValidCpf, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return CPFValidator.isValid(value);
    }
}
