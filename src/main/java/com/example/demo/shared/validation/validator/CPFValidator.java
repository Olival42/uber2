package com.example.demo.shared.validation.validator;

public final class CPFValidator {

    private CPFValidator() {}

    public static boolean isValid(String value) {
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
