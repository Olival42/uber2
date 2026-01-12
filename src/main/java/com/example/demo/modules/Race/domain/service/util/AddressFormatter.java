package com.example.demo.modules.Race.domain.service.util;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.demo.modules.Race.domain.entity.AddressEntity;

public final class AddressFormatter {

    private AddressFormatter() {}

    public static String buildFullAddress(AddressEntity address) {
        return Stream.of(
                address.getStreet(),
                address.getNumber(),
                address.getNeighborhood(),
                address.getCity(),
                address.getPostalCode(),
                "Brazil"
            )
            .filter(Objects::nonNull)
            .collect(Collectors.joining(", "));
    }
}
