package com.example.demo.modules.User.application.web.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.modules.User.application.web.dto.AuthResponseDTO;
import com.example.demo.modules.User.application.web.dto.UserLoginDTO;
import com.example.demo.modules.User.domain.service.UserService;
import com.example.demo.modules.User.infrastructure.security.service.JwtService;
import com.example.demo.shared.ApiResponse;
import com.example.demo.utils.CookieManager;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CookieManager cookieManager;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody @Valid UserLoginDTO req) {
        AuthResponseDTO authResponseDTO = userService.loginUser(req);

        var tokens = Map.of(
                "accessToken", authResponseDTO.getTokens().getAccessToken(),
                "expiresAt", authResponseDTO.getTokens().getExpiresAt());

        ApiResponse<?> response = ApiResponse.builder()
                .success(true)
                .data(Map.of(
                        "user", authResponseDTO.getUser(),
                        "tokens", tokens))
                .error(null)
                .build();

        ResponseCookie cookie = cookieManager.createRefreshCookie(
                authResponseDTO.getTokens().getRefreshToken(),
                jwtService.getExpiresAt(authResponseDTO.getTokens().getRefreshToken())
                        - Instant.now().getEpochSecond());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {

        userService.logoutUser(refreshToken);

        ApiResponse<?> response = ApiResponse.builder()
                .success(true)
                .data(Map.of("message", "Logout successful"))
                .error(null)
                .build();

        ResponseCookie cookie = cookieManager.deleteRefreshCookie();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }
}
