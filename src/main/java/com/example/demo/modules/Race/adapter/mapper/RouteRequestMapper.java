package com.example.demo.modules.Race.adapter.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.modules.Race.application.web.dto.request.RouteRequestDTO;
import com.example.demo.modules.Race.domain.entity.AddressEntity;

@Component
public class RouteRequestMapper {

    public RouteRequestDTO fromAddresses(AddressEntity pickupAddress, AddressEntity destinationAddress) {
        return new RouteRequestDTO(
                new double[][] {
                        {
                                Double.parseDouble(pickupAddress.getLongitude()),
                                Double.parseDouble(pickupAddress.getLatitude())
                        },
                        {
                                Double.parseDouble(destinationAddress.getLongitude()),
                                Double.parseDouble(destinationAddress.getLatitude())
                        }
                });
    }
}
