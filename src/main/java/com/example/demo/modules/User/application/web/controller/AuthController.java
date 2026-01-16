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
import com.example.demo.modules.User.application.web.dto.AuthDTO;
import com.example.demo.modules.User.application.web.dto.AuthResponseDTO;
import com.example.demo.modules.User.application.web.dto.AuthTokenDTO;
import com.example.demo.modules.User.application.web.dto.TokenResponseDTO;
import com.example.demo.modules.User.application.web.dto.UserForgotPasswordRequestDTO;
import com.example.demo.modules.User.application.web.dto.UserLoginDTO;
import com.example.demo.modules.User.application.web.dto.UserResetPasswordRequestDTO;
import com.example.demo.modules.User.domain.service.UserService;
import com.example.demo.modules.User.infrastructure.security.service.JwtService;
import com.example.demo.shared.ApiResponse;
import com.example.demo.shared.MessageResponse;
import com.example.demo.shared.mapper.ApiMapper;
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
                AuthDTO authResponseDTO = userService.loginUser(req);

                var tokens = new TokenResponseDTO(authResponseDTO.tokens().accessToken(),
                                authResponseDTO.tokens().expiresAt());

                var response = ApiMapper.sucess(new AuthResponseDTO(authResponseDTO.user(), tokens));

                ResponseCookie cookie = cookieManager.createRefreshCookie(
                                authResponseDTO.tokens().refreshToken(),
                                jwtService.getExpiresAt(authResponseDTO.tokens().refreshToken())
                                                - Instant.now().getEpochSecond());

                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
        }

        @PostMapping("/logout")
        public ResponseEntity<ApiResponse<?>> logout(
                        @CookieValue(value = "refreshToken", required = false) String refreshToken,
                        @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String header) {

                if (refreshToken == null && header == null) {
                        return ResponseEntity.ok(ApiMapper.sucess(new MessageResponse("Already logged out")));
                }

                if (header != null && header.startsWith("Bearer ")) {
                        String accessToken = header.replace("Bearer ", "");
                        userService.logoutUser(accessToken, refreshToken);
                }

                var response = ApiMapper.sucess(new MessageResponse("Logout successful"));

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

                var tokens = new TokenResponseDTO(authResponseDTO.accessToken(),
                                authResponseDTO.expiresAt());

                var response = ApiMapper.sucess(Map.of("tokens", tokens));

                ResponseCookie cookie = cookieManager.createRefreshCookie(
                                authResponseDTO.refreshToken(),
                                jwtService.getExpiresAt(authResponseDTO.refreshToken())
                                                - Instant.now().getEpochSecond());

                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
        }

        @PostMapping("/forgot-password")
        public ResponseEntity<ApiResponse<?>> forgotPassword(@RequestBody @Valid UserForgotPasswordRequestDTO req) {
                userService.sendResetPasswordEmail(req.getEmail());

                var response = ApiMapper.sucess(new MessageResponse("If the email exists, a reset link has been sent"));

                return ResponseEntity.ok().body(response);
        }

        @PostMapping("/reset-password")
        public ResponseEntity<ApiResponse<?>> resetPassword(@RequestBody @Valid UserResetPasswordRequestDTO req) {
                userService.resetPassword(req.getToken(), req.getNewPassword());

                var response = ApiMapper.sucess(new MessageResponse("Password has been reset successfully"));

                return ResponseEntity.ok().body(response);
        }
}
