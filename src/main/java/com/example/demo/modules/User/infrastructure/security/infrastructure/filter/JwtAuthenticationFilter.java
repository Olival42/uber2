package com.example.demo.modules.User.infrastructure.security.infrastructure.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.infrastructure.exception.security.InvalidTokenException;
import com.example.demo.modules.User.infrastructure.security.service.BlacklistService;
import com.example.demo.modules.User.infrastructure.security.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            jwtService.validateAccessToken(token);

            if (blacklistService.isTokenBlacklisted(token)) {
                throw new InvalidTokenException("Token revoked");
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                String subject = jwtService.getSubject(token);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        subject,
                        null,
                        List.of());

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

        } catch (InvalidTokenException e) {
            SecurityContextHolder.clearContext();
            throw new BadCredentialsException("Invalid token", e);
        }

        filterChain.doFilter(request, response);
    }
}
