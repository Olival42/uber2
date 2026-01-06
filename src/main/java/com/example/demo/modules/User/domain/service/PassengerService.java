package com.example.demo.modules.User.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.modules.User.adapter.mapper.UserMapper;
import com.example.demo.modules.User.application.web.dto.PassengerResponseDTO;
import com.example.demo.modules.User.domain.entity.PassengerEntity;
import com.example.demo.modules.User.domain.repository.IPassengerRepository;
import com.example.demo.modules.User.infrastructure.security.service.AuthContextService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PassengerService {

    @Autowired
    private IPassengerRepository passengerRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthContextService authService;

    public PassengerResponseDTO getPassengerById() {
        String currentUserEmail = authService.getAuthenticatedUser();

        PassengerEntity passenger = passengerRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("Passenger not found"));

        return userMapper.toPassengerResponseDto(passenger);
    }
}
