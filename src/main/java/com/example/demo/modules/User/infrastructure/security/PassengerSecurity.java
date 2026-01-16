package com.example.demo.modules.User.infrastructure.security;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.modules.User.domain.repository.IPassengerRepository;
import com.example.demo.shared.security.SecurityUtils;

@Component
public class PassengerSecurity {

    @Autowired
    private IPassengerRepository passengerRepository;

    public boolean isOwner(UUID passengerId) {
        String email = SecurityUtils.getEmail();
        return passengerRepository.findById(passengerId)
                .map(passenger -> passenger.getEmail().equals(email))
                .orElse(false);
    }

}
