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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.infrastructure.exception.security.MissingTokenException;
import com.example.demo.modules.User.application.web.dto.AuthResponseDTO;
import com.example.demo.modules.User.application.web.dto.AuthTokenDTO;
import com.example.demo.modules.User.application.web.dto.UserForgotPasswordRequestDTO;
import com.example.demo.modules.User.application.web.dto.UserLoginDTO;
import com.example.demo.modules.User.application.web.dto.UserResetPasswordRequestDTO;
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
                        @CookieValue(value = "refreshToken", required = false) String refreshToken,
                        @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String header) {

                if (refreshToken == null && header == null) {
                        return ResponseEntity.ok(
                                        ApiResponse.builder()
                                                        .success(true)
                                                        .data(Map.of("message", "Already logged out"))
                                                        .error(null)
                                                        .build());
                }

                if (header != null && !header.startsWith("Bearer ")) {
                        String accessToken = header.replace("Bearer ", "");
                        userService.logoutUser(accessToken, refreshToken);
                }

                ApiResponse<?> response = ApiResponse.builder()
                                .success(true)
                                .data(Map.of("message", "Logout successful"))
                                .error(null)
                                .build();

                ResponseCookie cookie = cookieManager.deleteRefreshCookie();

                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
        }

        @PostMapping("/refresh-token")
        public ResponseEntity<ApiResponse<?>> refresh(
                        @CookieValue(value = "refreshToken", required = false) String refreshToken) {

                if (refreshToken == null || refreshToken.isBlank()) {
                        throw new MissingTokenException("Refresh token must not be null");
                }

                AuthTokenDTO authResponseDTO = userService.refreshTokens(refreshToken);

                var tokens = Map.of(
                                "accessToken", authResponseDTO.getAccessToken(),
                                "expiresAt", authResponseDTO.getExpiresAt());

                ApiResponse<?> response = ApiResponse.builder()
                                .success(true)
                                .data(Map.of(
                                                "tokens", tokens))
                                .error(null)
                                .build();

                ResponseCookie cookie = cookieManager.createRefreshCookie(
                                authResponseDTO.getRefreshToken(),
                                jwtService.getExpiresAt(authResponseDTO.getRefreshToken())
                                                - Instant.now().getEpochSecond());

                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
        }

        @PostMapping("/forgot-password")
        public ResponseEntity<ApiResponse<?>> forgotPassword(@RequestBody @Valid UserForgotPasswordRequestDTO req) {
                userService.sendResetPasswordEmail(req.getEmail());

                ApiResponse<?> response = ApiResponse.builder()
                                .success(true)
                                .data(Map.of("message", "If the email exists, a reset link has been sent"))
                                .error(null)
                                .build();

                return ResponseEntity.ok().body(response);
        }

        @PostMapping("/reset-password")
        public ResponseEntity<ApiResponse<?>> resetPassword(@RequestBody @Valid UserResetPasswordRequestDTO req) {
                userService.resetPassword(req.getToken(), req.getNewPassword());

                ApiResponse<?> response = ApiResponse.builder()
                                .success(true)
                                .data(Map.of("message", "Password has been reset successfully"))
                                .error(null)
                                .build();

                return ResponseEntity.ok().body(response);
        }
}
