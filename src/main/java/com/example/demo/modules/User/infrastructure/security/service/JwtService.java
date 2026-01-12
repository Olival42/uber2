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
                .claim("roles", List.of(user.getRole().name()))
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
                .claim("type", "refresh")
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String getSubject(String token) {
        return decoder.decode(token).getSubject();
    }

    public long getExpiresAt(String token) {
        Jwt jwt = decoder.decode(token);

        Instant expiresAt = jwt.getExpiresAt();
        if (expiresAt == null) {
            throw new IllegalStateException("Token não possui campo 'exp'");
        }

        return expiresAt.getEpochSecond();
    }

    public List<String> getRoles(String token) {
        Jwt jwt = decoder.decode(token);

        var rolesClaim = jwt.getClaims().get("roles");
        if (rolesClaim == null) {
            throw new InvalidTokenException("Token doesn't have 'roles' claim");
        }

        return (List<String>) rolesClaim; 
    }

    private String getTokenType(String token) {
        Jwt jwt = decoder.decode(token);

        var typeClaim = jwt.getClaims().get("type");
        if (typeClaim == null) {
            throw new IllegalStateException("Token doesn't have 'type' claim");
        }

        return typeClaim.toString();
    }

    public boolean isAccessTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);

            String type = getTokenType(token);
            return "access".equals(type);

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isRefreshTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);

            String type = getTokenType(token);
            return "refresh".equals(type);

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public void validateAccessTokenOrThrow(String token) {
        try {
            Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);

            String type = getTokenType(token);
            if (!"access".equals(type)) {
                throw new InvalidTokenTypeException("Token isn't an access token");
            }

        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Invalid JWT token", e);
        }
    }

    public void validateRefreshTokenOrThrow(String token) {
        try {
            Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);

            String type = getTokenType(token);
            if (!"refresh".equals(type)) {
                throw new InvalidTokenTypeException("Token isn't a refresh token");
            }

        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Invalid JWT token", e);
        }
    }
}