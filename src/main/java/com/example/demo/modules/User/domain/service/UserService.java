package com.example.demo.modules.User.domain.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.modules.User.adapter.mapper.UserMapper;
import com.example.demo.modules.User.application.web.dto.RegisterDriverDTO;
import com.example.demo.modules.User.application.web.dto.RegisterPassengerDTO;
import com.example.demo.modules.User.application.web.dto.RegisterResponseDTO;
import com.example.demo.modules.User.application.web.dto.UserResponseDTO;
import com.example.demo.modules.User.domain.entity.AuthTokenDTO;
import com.example.demo.modules.User.domain.entity.DriverEntity;
import com.example.demo.modules.User.domain.entity.PassengerEntity;
import com.example.demo.modules.User.domain.entity.UserEntity;
import com.example.demo.modules.User.domain.repository.IDriverRepository;
import com.example.demo.modules.User.domain.repository.IPassengerRepository;
import com.example.demo.modules.User.infrastructure.security.service.JwtService;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private IPassengerRepository passengerRepository;

    @Autowired
    private IDriverRepository driverRepository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder encoder;

    @Transactional
    public RegisterResponseDTO registerPassenger(RegisterPassengerDTO req) {
        PassengerEntity entity = mapper.toPassengerEntity(req);

        entity.setPassword(encoder.encode(entity.getPassword()));

        passengerRepository.save(entity);

        AuthTokenDTO tokens = generateTokens(entity);
        UserResponseDTO userResponse = mapper.toUserResponseDto(entity);

        return RegisterResponseDTO.builder()
                .user(userResponse)
                .tokens(tokens)
                .build();
    }

    @Transactional
    public RegisterResponseDTO registerDriver(RegisterDriverDTO req) {
        DriverEntity entity = mapper.toDriverEntity(req);

        entity.setPassword(encoder.encode(entity.getPassword()));

        driverRepository.save(entity);

        AuthTokenDTO tokens = generateTokens(entity);
        UserResponseDTO userResponse = mapper.toUserResponseDto(entity);

        return RegisterResponseDTO.builder()
                .user(userResponse)
                .tokens(tokens)
                .build();
    }

    private AuthTokenDTO generateTokens(UserEntity entity) {
        AuthTokenDTO authTokenDTO = new AuthTokenDTO();

        String accessToken = jwtService.generateAccessToken(entity);
        String refreshToken = jwtService.generateRefreshToken(entity);
        long expiresAt = jwtService.getExpiresAt(accessToken);

        authTokenDTO.setAccessToken(accessToken);
        authTokenDTO.setRefreshToken(refreshToken);
        authTokenDTO.setExpiresAt(expiresAt);

        return authTokenDTO;
    }
}
