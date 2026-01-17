package com.example.demo.shared.validation.validator;

import java.util.regex.Pattern;

public final class PhoneValidator {

    private PhoneValidator() {}

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "\\d{2}\\d{4,5}\\d{4}",
            Pattern.CASE_INSENSITIVE);

    public static boolean isValid(String value) {

        if (value == null || value.isBlank()) return false;

        return PHONE_PATTERN.matcher(value).matches();
    }

}
