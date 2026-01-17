package com.example.demo.shared.validation.validator;

import java.util.regex.Pattern;

public final class EmailValidator {

    private EmailValidator() {}

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE);

    public static boolean isValid(String value) {

        if (value == null || value.isBlank()) return false;

        return EMAIL_PATTERN.matcher(value).matches();
    }
}
