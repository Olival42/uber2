package com.example.demo.modules.User.infrastructure.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.infrastructure.exception.security.InsufficientAuthenticationException;

@Service
public class AuthContextService {

    @Autowired
    private JwtService jwtService;

    public String getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new InsufficientAuthenticationException("User not authenticated");
        }

        return auth.getName();
    }

    public void validateRefreshTokenOwnership(String refreshToken) {
        jwtService.validateRefreshTokenOrThrow(refreshToken);

        String subject = jwtService.getSubject(refreshToken);
        String authenticatedUser = getAuthenticatedUser();

        if (!subject.equals(authenticatedUser)) {
            throw new InsufficientAuthenticationException(
                    "Refresh token does not belong to the authenticated user");
        }
    }
}
