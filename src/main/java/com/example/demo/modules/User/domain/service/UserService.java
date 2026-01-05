package com.example.demo.modules.User.domain.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.infrastructure.exception.security.MissingTokenException;
import com.example.demo.modules.User.adapter.mapper.UserMapper;
import com.example.demo.modules.User.application.web.dto.AuthResponseDTO;
import com.example.demo.modules.User.application.web.dto.AuthTokenDTO;
import com.example.demo.modules.User.application.web.dto.RegisterDriverDTO;
import com.example.demo.modules.User.application.web.dto.RegisterPassengerDTO;
import com.example.demo.modules.User.application.web.dto.UserLoginDTO;
import com.example.demo.modules.User.application.web.dto.UserResponseDTO;
import com.example.demo.modules.User.domain.entity.DriverEntity;
import com.example.demo.modules.User.domain.entity.PassengerEntity;
import com.example.demo.modules.User.domain.entity.UserEntity;
import com.example.demo.modules.User.domain.repository.IDriverRepository;
import com.example.demo.modules.User.domain.repository.IPassengerRepository;
import com.example.demo.modules.User.infrastructure.security.service.BlacklistService;
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

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponseDTO registerPassenger(RegisterPassengerDTO req) {
        PassengerEntity entity = mapper.toPassengerEntity(req);

        entity.setPassword(encoder.encode(entity.getPassword()));

        passengerRepository.save(entity);

        AuthTokenDTO tokens = generateTokens(entity);
        UserResponseDTO userResponse = mapper.toUserResponseDto(entity);

        return AuthResponseDTO.builder()
                .user(userResponse)
                .tokens(tokens)
                .build();
    }

    @Transactional
    public AuthResponseDTO registerDriver(RegisterDriverDTO req) {
        DriverEntity entity = mapper.toDriverEntity(req);

        entity.setPassword(encoder.encode(entity.getPassword()));

        driverRepository.save(entity);

        AuthTokenDTO tokens = generateTokens(entity);
        UserResponseDTO userResponse = mapper.toUserResponseDto(entity);

        return AuthResponseDTO.builder()
                .user(userResponse)
                .tokens(tokens)
                .build();
    }

    public AuthResponseDTO loginUser(UserLoginDTO req) {
        UsernamePasswordAuthenticationToken userNamePassword = new UsernamePasswordAuthenticationToken(req.getEmail(),
                req.getPassword());

        Authentication auth = this.authenticationManager.authenticate(userNamePassword);

        UserEntity entity = (UserEntity) auth.getPrincipal();

        AuthTokenDTO tokens = generateTokens(entity);
        UserResponseDTO userResponse = mapper.toUserResponseDto(entity);

        return AuthResponseDTO.builder()
                .user(userResponse)
                .tokens(tokens)
                .build();
    }

    @Transactional
    public void logoutUser(String refreshToken) {

        if (refreshToken.isBlank()) {
            throw new MissingTokenException("Tokens must not be null");
        }

        jwtService.validateRefreshToken(refreshToken);

        String userFromContext = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!jwtService.getSubject(refreshToken).equals(userFromContext)) {
            throw new SecurityException("Invalid refresh token");
        }

        long ttlRefresh = jwtService.getExpiresAt(refreshToken) - Instant.now().getEpochSecond();

        if (!blacklistService.isTokenBlacklisted(refreshToken) && ttlRefresh > 0) {
            blacklistService.blacklistToken(refreshToken, ttlRefresh);
        }
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
