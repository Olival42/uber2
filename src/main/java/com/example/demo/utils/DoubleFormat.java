package com.example.demo.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleFormat {

    public static double round(double value, int places) {
        return BigDecimal.valueOf(value)
                .setScale(places, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
