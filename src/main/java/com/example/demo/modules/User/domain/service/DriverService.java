package com.example.demo.modules.User.domain.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.modules.User.adapter.mapper.UserMapper;
import com.example.demo.modules.User.application.validation.validator.PixKeyTypeValidator;
import com.example.demo.modules.User.application.web.dto.request.PixKeyRegisterRequestDTO;
import com.example.demo.modules.User.application.web.dto.response.DriverResponseDTO;
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

    @Autowired
    private PixKeyTypeValidator pixKeyTypeValidator;

    public DriverResponseDTO getDriverById() {
        String currentUserEmail = authService.getAuthenticatedUser();

        DriverEntity driver = driverRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        return userMapper.toDriverResponseDto(driver);
    }

    @Transactional
    public void registerPixKey(UUID driverId, PixKeyRegisterRequestDTO req) {

        DriverEntity driverEntity = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        pixKeyTypeValidator.validate(req.getKey(), req.getType());

        driverEntity.setPixKey(req.getKey());
        driverEntity.setPixKeyType(req.getType());

        driverRepository.save(driverEntity);
    }

}
