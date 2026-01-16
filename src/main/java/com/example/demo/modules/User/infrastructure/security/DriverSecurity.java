package com.example.demo.modules.User.infrastructure.security;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.modules.User.domain.repository.IDriverRepository;
import com.example.demo.shared.security.SecurityUtils;

@Component
public class DriverSecurity {

    @Autowired
    private IDriverRepository driverRepository;

    public boolean isOwner(UUID driverId) {
        String email = SecurityUtils.getEmail();
        return driverRepository.findById(driverId)
                .map(driver -> driver.getEmail().equals(email))
                .orElse(false);
    }
}
