package com.example.demo.shared.validation.validator;

public final class CNPJValidator {

    private CNPJValidator() {}

    public static boolean isValid(String value) {

        if (value == null) return false;

        String cnpj = value.replaceAll("\\D", "");

        if (!cnpj.matches("\\d{14}")) return false;

        if (cnpj.chars().distinct().count() == 1) return false;

        int[] weight1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] weight2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += Character.getNumericValue(cnpj.charAt(i)) * weight1[i];
        }

        int remainder = sum % 11;
        int firstDigit = (remainder < 2) ? 0 : 11 - remainder;

        sum = 0;
        for (int i = 0; i < 13; i++) {
            sum += Character.getNumericValue(cnpj.charAt(i)) * weight2[i];
        }

        remainder = sum % 11;
        int secondDigit = (remainder < 2) ? 0 : 11 - remainder;

        return firstDigit == Character.getNumericValue(cnpj.charAt(12))
            && secondDigit == Character.getNumericValue(cnpj.charAt(13));
    }
}

