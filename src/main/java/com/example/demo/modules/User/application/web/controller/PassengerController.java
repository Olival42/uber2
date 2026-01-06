package com.example.demo.modules.User.application.web.controller;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.modules.User.application.web.dto.AuthResponseDTO;
import com.example.demo.modules.User.application.web.dto.PassengerResponseDTO;
import com.example.demo.modules.User.application.web.dto.RegisterPassengerDTO;
import com.example.demo.modules.User.domain.service.PassengerService;
import com.example.demo.modules.User.domain.service.UserService;
import com.example.demo.modules.User.infrastructure.security.service.JwtService;
import com.example.demo.shared.ApiResponse;
import com.example.demo.utils.CookieManager;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

        @Autowired
        private UserService userService;

        @Autowired
        private PassengerService passengerService;

        @Autowired
        private JwtService jwtService;

        @Autowired
        private CookieManager cookieManager;

        @PostMapping("/register")
        public ResponseEntity<ApiResponse<?>> postMethodName(@RequestBody @Valid RegisterPassengerDTO req,
                        UriComponentsBuilder uriBuilder) {
                AuthResponseDTO authResponseDTO = userService.registerPassenger(req);

                URI url = uriBuilder.path("/passengers/{id}").buildAndExpand(authResponseDTO.getUser().getId())
                                .toUri();

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

                ResponseCookie cookie = cookieManager.createRefreshCookie(authResponseDTO.getTokens().getRefreshToken(),
                                jwtService.getExpiresAt(authResponseDTO.getTokens().getRefreshToken())
                                                - Instant.now().getEpochSecond());

                return ResponseEntity.created(url).header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
        }

        @GetMapping("/me")
        public ResponseEntity<ApiResponse<?>> getMyProfile() {
                PassengerResponseDTO passenger = passengerService.getPassengerById();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(true)
                                .data(Map.of("passenger", passenger))
                                .error(null)
                                .build();

                return ResponseEntity.ok(response);
        }

}
