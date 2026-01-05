package com.example.demo.modules.User.application.web.controller;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.modules.User.domain.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.modules.User.application.web.dto.RegisterPassengerDTO;
import com.example.demo.modules.User.application.web.dto.RegisterResponseDTO;
import com.example.demo.modules.User.infrastructure.security.service.JwtService;
import com.example.demo.shared.ApiResponse;
import com.example.demo.utils.CookieManager;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

        @Autowired
        private UserService service;

        @Autowired
        private JwtService jwtService;

        @Autowired
        private CookieManager cookieManager;

        @PostMapping("/register")
        public ResponseEntity<ApiResponse<?>> postMethodName(@RequestBody @Valid RegisterPassengerDTO req,
                        UriComponentsBuilder uriBuilder) {
                RegisterResponseDTO registerResponseDTO = service.registerPassenger(req);

                URI url = uriBuilder.path("/passengers/{id}").buildAndExpand(registerResponseDTO.getUser().getId())
                                .toUri();

                var tokens = Map.of(
                                "accessToken", registerResponseDTO.getTokens().getAccessToken(),
                                "expiresAt", registerResponseDTO.getTokens().getExpiresAt());

                ApiResponse<?> response = ApiResponse.builder()
                                .success(true)
                                .data(Map.of(
                                                "user", registerResponseDTO.getUser(),
                                                "tokens", tokens))
                                .error(null)
                                .build();

                ResponseCookie cookie = cookieManager.createRefreshCookie(registerResponseDTO.getTokens().getRefreshToken(),
                                jwtService.getExpiresAt(registerResponseDTO.getTokens().getRefreshToken())
                                                - Instant.now().getEpochSecond());

                return ResponseEntity.created(url).header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
        }

        

}
