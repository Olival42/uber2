package com.example.demo.modules.User.infrastructure.security.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.example.demo.modules.User.domain.entity.UserEntity;
import com.example.demo.modules.User.infrastructure.security.config.JwtProperties;

@Service
public class JwtService {

    @Autowired
    private JwtDecoder decoder;

    @Autowired
    private JwtEncoder encoder;

    @Autowired
    private JwtProperties jwtProperties;

    public String generateAccessToken(UserEntity user) {
        Instant now = Instant.now();
        long expiresAt = now.plus(jwtProperties.getAccessTokenExpiration()).getEpochSecond() - now.getEpochSecond();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("uber2")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresAt))
                .subject(user.getEmail())
                .claim("role", List.of(user.getRole().name()))
                .claim("type", "refresh")
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateRefreshToken(UserEntity user) {
        Instant now = Instant.now();
        long expiresAt = now.plus(jwtProperties.getRefreshTokenExpiration()).getEpochSecond() - now.getEpochSecond();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("uber2")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresAt))
                .subject(user.getEmail())
                .claim("role", List.of(user.getRole().name()))
                .claim("type", "refresh")
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String getSubject(String token) {
        return decoder.decode(token).getSubject();
    }

    public long getExpiresAt(String token) {
        Jwt jwt = decoder.decode(token);

        if (jwt.getExpiresAt() == null) {
            throw new IllegalStateException("Token não possui campo 'exp'");
        }

        return jwt.getExpiresAt().getEpochSecond();
    }
}
