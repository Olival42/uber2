package com.example.demo.modules.Race.adapter.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.modules.Race.application.web.dto.ResponseRideDTO;
import com.example.demo.modules.Race.application.web.dto.RouteInfo;
import com.example.demo.modules.Race.domain.entity.AddressEntity;
import com.example.demo.modules.Race.domain.entity.RideEntity;
import com.example.demo.modules.User.domain.entity.PassengerEntity;
import com.example.demo.utils.DoubleFormat;

@Component
public class RideMapper {

    public RideEntity toEntity(PassengerEntity passenger, AddressEntity pickupAddress, AddressEntity destinationAddress,
            Double price, RouteInfo routeInfo) {
        return RideEntity.builder()
                .passenger(passenger)
                .driver(null)
                .pickupAddress(pickupAddress)
                .destinationAddress(destinationAddress)
                .price(price)
                .distanceInKm(routeInfo.distanceKm())
                .durationInMinutes(routeInfo.durationMinutes())
                .startedAt(null)
                .build();
    }

    public ResponseRideDTO toResponseDTO(RideEntity rideEntity) {
        return ResponseRideDTO.builder()
                .rideId(rideEntity.getId())
                .distanceKm(DoubleFormat.round(rideEntity.getDistanceInKm(), 1))
                .durationMinutes((int) Math.ceil(rideEntity.getDurationInMinutes()))
                .price(DoubleFormat.round(rideEntity.getPrice(), 2))
                .passengerId(rideEntity.getPassenger().getId())
                .status(rideEntity.getStatus())
                .requestedAt(rideEntity.getRequestedAt())
                .build();
    }
}
