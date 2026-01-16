package com.example.demo.modules.Race.infrastructure.security;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.modules.Race.domain.repository.IRideRepository;
import com.example.demo.modules.User.domain.repository.IDriverRepository;
import com.example.demo.modules.User.domain.repository.IPassengerRepository;
import com.example.demo.shared.security.SecurityUtils;

@Component
public class RideSecurity {

    @Autowired
    private IRideRepository rideRepository;

    @Autowired
    private IPassengerRepository passengerRepository;

    @Autowired
    private IDriverRepository driverRepository;

    public boolean isPassengerOwner(UUID rideId) {
        String email = SecurityUtils.getEmail();
        return passengerRepository.findByEmail(email)
                .map(passenger -> rideRepository.findByIdAndPassengerId(rideId, passenger.getId()).isPresent())
                .orElse(false);
    }

    public boolean isDriverOwner(UUID rideId) {
        String email = SecurityUtils.getEmail();
        return driverRepository.findByEmail(email)
                .map(driver -> rideRepository.findByIdAndDriverId(rideId, driver.getId()).isPresent())
                .orElse(false);
    }

}
