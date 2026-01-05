package com.example.demo.modules.User.infrastructure.security.service;

import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import com.example.demo.infrastructure.exception.security.InvalidTokenException;
import com.example.demo.infrastructure.exception.security.InvalidTokenTypeException;
import com.example.demo.modules.User.domain.entity.UserEntity;
import com.example.demo.modules.User.infrastructure.security.config.JwtProperties;

import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    @Autowired
    private JwtDecoder decoder;

    @Autowired
    private JwtEncoder encoder;

    @Autowired
    private JwtProperties jwtProperties;

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    public String generateAccessToken(UserEntity user) {
        Instant now = Instant.now();
        long expiresAt = now.plus(jwtProperties.getAccessTokenExpiration()).getEpochSecond() - now.getEpochSecond();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("uber2")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresAt))
                .subject(user.getEmail())
                .claim("role", List.of(user.getRole().name()))
                .claim("type", "access")
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

    private String getTokenType(String token) {
        Jwt jwt = decoder.decode(token);

        var typeClaim = jwt.getClaims().get("type");
        if (typeClaim == null) {
            throw new IllegalStateException("Token doesn't have 'type' claim");
        }

        return typeClaim.toString();
    }

    private void validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);

        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Invalid JWT token", e);
        }
    }

    public void validateAccessToken(String token) {
        validate(token);

        String type = getTokenType(token);
        if (!type.equals("access")) {
            throw new InvalidTokenTypeException("Token isn't an access token");
        }
    }

    public void validateRefreshToken(String token) {
        validate(token);

        String type = getTokenType(token);
        if (!type.equals("refresh")) {
            throw new InvalidTokenTypeException("Token isn't a refresh token");
        }
    }
}