package com.example.demo.modules.User.application.validation.validator;

import com.example.demo.modules.User.application.validation.anotation.ValidCpf;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfConstraintValidator implements ConstraintValidator<ValidCpf, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String cpf = value.replaceAll("[^\\d]", "");

        if (!cpf.matches("\\d{11}"))
            return false;

        if (cpf.chars().distinct().count() == 1)
            return false;

        var j = 10;
        var sumFirstDigit = 0;
        var sumSecondDigit = 0;

        for (var i = 0; i < 9; i++) {
            sumFirstDigit = sumFirstDigit + Character.getNumericValue(cpf.charAt(i)) * j;
            j--;
        }

        j = 11;
        for (var i = 0; i < 10; i++) {
            sumSecondDigit = sumSecondDigit + Character.getNumericValue(cpf.charAt(i)) * j;
            j--;
        }

        var remainderFirstDigit = sumFirstDigit * 10 % 11;
        if (remainderFirstDigit == 10) {
            remainderFirstDigit = 0;
        }

        var remainderSecondDigit = sumSecondDigit * 10 % 11;

        return remainderFirstDigit == Character.getNumericValue(cpf.charAt(9))
                && remainderSecondDigit == Character.getNumericValue(cpf.charAt(10));
    }
}
