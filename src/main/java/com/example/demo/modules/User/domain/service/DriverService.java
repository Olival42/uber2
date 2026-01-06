package com.example.demo.modules.User.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.modules.User.adapter.mapper.UserMapper;
import com.example.demo.modules.User.application.web.dto.DriverResponseDTO;
import com.example.demo.modules.User.domain.entity.DriverEntity;
import com.example.demo.modules.User.domain.repository.IDriverRepository;
import com.example.demo.modules.User.infrastructure.security.service.AuthContextService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DriverService {

    @Autowired
    private IDriverRepository driverRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthContextService authService;

    public DriverResponseDTO getDriverById() {
        String currentUserEmail = authService.getAuthenticatedUser();

        DriverEntity driver = driverRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        return userMapper.toDriverResponseDto(driver);
    }
}
