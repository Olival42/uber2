package com.example.demo.modules.User.domain.service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.infrastructure.exception.security.MissingTokenException;
import com.example.demo.infrastructure.exception.security.TokenRevokedException;
import com.example.demo.modules.User.adapter.mapper.UserMapper;
import com.example.demo.modules.User.application.web.dto.AuthDTO;
import com.example.demo.modules.User.application.web.dto.AuthTokenDTO;
import com.example.demo.modules.User.application.web.dto.RegisterDriverDTO;
import com.example.demo.modules.User.application.web.dto.RegisterPassengerDTO;
import com.example.demo.modules.User.application.web.dto.UserLoginDTO;
import com.example.demo.modules.User.application.web.dto.UserResponseDTO;
import com.example.demo.modules.User.domain.entity.DriverEntity;
import com.example.demo.modules.User.domain.entity.PassengerEntity;
import com.example.demo.modules.User.domain.entity.ResetPasswordTokenEntity;
import com.example.demo.modules.User.domain.entity.UserEntity;
import com.example.demo.modules.User.domain.repository.IDriverRepository;
import com.example.demo.modules.User.domain.repository.IPassengerRepository;
import com.example.demo.modules.User.domain.repository.IResetPassowordRepository;
import com.example.demo.modules.User.domain.repository.IUserRepository;
import com.example.demo.modules.User.infrastructure.security.service.AuthContextService;
import com.example.demo.modules.User.infrastructure.security.service.BlacklistService;
import com.example.demo.modules.User.infrastructure.security.service.JwtService;
import com.example.demo.shared.email.EmailService;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private IPassengerRepository passengerRepository;

    @Autowired
    private IDriverRepository driverRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IResetPassowordRepository resetPassowordRepository;

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

    @Autowired
    private AuthContextService authContextService;

    @Autowired
    private EmailService emailService;

    @Transactional
    public AuthDTO registerPassenger(RegisterPassengerDTO req) {
        PassengerEntity entity = mapper.toPassengerEntity(req);

        entity.setPassword(encoder.encode(entity.getPassword()));

        var savedEntity = passengerRepository.save(entity);

        AuthTokenDTO tokens = generateTokens(savedEntity);
        UserResponseDTO userResponse = mapper.toUserResponseDto(savedEntity);

        return new AuthDTO(userResponse, tokens);
    }

    @Transactional
    public AuthDTO registerDriver(RegisterDriverDTO req) {
        DriverEntity entity = mapper.toDriverEntity(req);

        entity.setPassword(encoder.encode(entity.getPassword()));

        var savedEntity = driverRepository.save(entity);

        AuthTokenDTO tokens = generateTokens(savedEntity);
        UserResponseDTO userResponse = mapper.toUserResponseDto(savedEntity);

        return new AuthDTO(userResponse, tokens);
    }

    public AuthDTO loginUser(UserLoginDTO req) {
        UsernamePasswordAuthenticationToken userNamePassword = new UsernamePasswordAuthenticationToken(req.getEmail(),
                req.getPassword());

        Authentication auth = this.authenticationManager.authenticate(userNamePassword);

        UserEntity entity = (UserEntity) auth.getPrincipal();

        AuthTokenDTO tokens = generateTokens(entity);
        UserResponseDTO userResponse = mapper.toUserResponseDto(entity);

        return new AuthDTO(userResponse, tokens);
    }

    public void logoutUser(String accessToken, String refreshToken) {

        if (refreshToken.isBlank() || accessToken.isBlank()) {
            throw new MissingTokenException("Tokens must not be null");
        }

        authContextService.validateRefreshTokenOwnership(refreshToken);

        long ttlRefresh = jwtService.getExpiresAt(refreshToken) - Instant.now().getEpochSecond();
        long ttlAccess = jwtService.getExpiresAt(accessToken) - Instant.now().getEpochSecond();

        if (!blacklistService.isTokenBlacklisted(accessToken) && ttlAccess > 0) {
            blacklistService.blacklistToken(accessToken, ttlAccess);
        }

        if (!blacklistService.isTokenBlacklisted(refreshToken) && ttlRefresh > 0) {
            blacklistService.blacklistToken(refreshToken, ttlRefresh);
        }
    }

    public AuthTokenDTO refreshTokens(String refreshToken) {
        jwtService.validateRefreshTokenOrThrow(refreshToken);

        if (blacklistService.isTokenBlacklisted(refreshToken)) {
            throw new TokenRevokedException("Refresh token is revoked");
        }

        String subject = jwtService.getSubject(refreshToken);

        UserEntity entity = userRepository.findByEmail(subject)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        blacklistService.blacklistToken(refreshToken,
                jwtService.getExpiresAt(refreshToken) - Instant.now().getEpochSecond());

        return generateTokens(entity);
    }

    @Transactional
    public void sendResetPasswordEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {

            resetPassowordRepository.deleteByUserEmail(user.getEmail());

            String token = UUID.randomUUID().toString();

            String hashedToken = encoder.encode(token);

            ResetPasswordTokenEntity resetTokenEntity = ResetPasswordTokenEntity.builder()
                    .token(hashedToken)
                    .userEmail(user.getEmail())
                    .expiration(Instant.now().plus(Duration.ofMinutes(15)))
                    .build();

            resetPassowordRepository.save(resetTokenEntity);

            String resetLink = "https://your-frontend-app.com/reset-password?token=" + token;

            emailService.sendResetPasswordEmail(user.getEmail(), resetLink);
        });
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        ResetPasswordTokenEntity resetTokenEntity = resetPassowordRepository.findValidToken(Instant.now())
                .stream()
                .filter(t -> encoder.matches(token, t.getToken()))
                .findFirst()
                .orElseThrow(() -> new MissingTokenException("Invalid or expired reset token"));

        if (resetTokenEntity.getExpiration().isBefore(Instant.now())) {
            throw new MissingTokenException("Reset token has expired");
        }

        UserEntity user = userRepository.findByEmail(resetTokenEntity.getUserEmail()).orElseThrow();

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        resetPassowordRepository.delete(resetTokenEntity);
    }

    private AuthTokenDTO generateTokens(UserEntity entity) {

        String accessToken = jwtService.generateAccessToken(entity);
        String refreshToken = jwtService.generateRefreshToken(entity);
        long expiresAt = jwtService.getExpiresAt(accessToken);

        return new AuthTokenDTO(accessToken, refreshToken, expiresAt);

    }
}
