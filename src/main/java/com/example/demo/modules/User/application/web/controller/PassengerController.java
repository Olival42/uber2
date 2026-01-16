package com.example.demo.modules.User.application.web.controller;

import java.net.URI;
import java.time.Instant;

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

import com.example.demo.modules.User.application.web.dto.AuthDTO;
import com.example.demo.modules.User.application.web.dto.AuthResponseDTO;
import com.example.demo.modules.User.application.web.dto.PassengerResponseDTO;
import com.example.demo.modules.User.application.web.dto.RegisterPassengerDTO;
import com.example.demo.modules.User.application.web.dto.TokenResponseDTO;
import com.example.demo.modules.User.domain.service.PassengerService;
import com.example.demo.modules.User.domain.service.UserService;
import com.example.demo.modules.User.infrastructure.security.service.JwtService;
import com.example.demo.shared.ApiResponse;
import com.example.demo.shared.mapper.ApiMapper;
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
        public ResponseEntity<ApiResponse<?>> registerPassenger(@RequestBody @Valid RegisterPassengerDTO req,
                        UriComponentsBuilder uriBuilder) {
                AuthDTO authResponseDTO = userService.registerPassenger(req);

                URI url = uriBuilder.path("/passengers/{id}").buildAndExpand(authResponseDTO.user().getId())
                                .toUri();

                var tokens = new TokenResponseDTO(authResponseDTO.tokens().accessToken(),
                                authResponseDTO.tokens().expiresAt());

                var response = ApiMapper.sucess(new AuthResponseDTO(authResponseDTO.user(), tokens));

                ResponseCookie cookie = cookieManager.createRefreshCookie(authResponseDTO.tokens().refreshToken(),
                                jwtService.getExpiresAt(authResponseDTO.tokens().refreshToken())
                                                - Instant.now().getEpochSecond());

                return ResponseEntity.created(url).header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
        }

        @GetMapping("/me")
        public ResponseEntity<ApiResponse<?>> getMyProfile() {
                PassengerResponseDTO passenger = passengerService.getPassengerById();

                var response = ApiMapper.sucess(passenger);

                return ResponseEntity.ok(response);
        }

}
