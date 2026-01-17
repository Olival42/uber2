package com.example.demo.modules.User.application.web.controller;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.modules.User.application.web.dto.AuthDTO;
import com.example.demo.modules.User.application.web.dto.request.PixKeyRegisterRequestDTO;
import com.example.demo.modules.User.application.web.dto.request.RegisterDriverRequestDTO;
import com.example.demo.modules.User.application.web.dto.response.AuthResponseDTO;
import com.example.demo.modules.User.application.web.dto.response.DriverResponseDTO;
import com.example.demo.modules.User.application.web.dto.response.TokenResponseDTO;
import com.example.demo.modules.User.domain.service.DriverService;
import com.example.demo.modules.User.domain.service.UserService;
import com.example.demo.modules.User.infrastructure.security.service.JwtService;
import com.example.demo.shared.ApiResponse;
import com.example.demo.shared.MessageResponse;
import com.example.demo.shared.mapper.ApiMapper;
import com.example.demo.utils.CookieManager;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/drivers")
@Validated
public class DriverController {

        @Autowired
        private UserService userService;

        @Autowired
        private DriverService driverService;

        @Autowired
        private JwtService jwtService;

        @Autowired
        private CookieManager cookieManager;

        @PostMapping("/register")
        public ResponseEntity<ApiResponse<?>> registerDriver(@RequestBody @Valid RegisterDriverRequestDTO req,
                        UriComponentsBuilder uriBuilder) {
                AuthDTO authResponseDTO = userService.registerDriver(req);

                URI url = uriBuilder.path("/drivers/{id}").buildAndExpand(authResponseDTO.user().getId())
                                .toUri();

                var tokens = new TokenResponseDTO(authResponseDTO.tokens().accessToken(),
                                authResponseDTO.tokens().expiresAt());

                var response = ApiMapper.sucess(new AuthResponseDTO(authResponseDTO.user(), tokens));

                ResponseCookie cookie = cookieManager.createRefreshCookie(
                                authResponseDTO.tokens().refreshToken(),
                                jwtService.getExpiresAt(authResponseDTO.tokens().refreshToken())
                                                - Instant.now().getEpochSecond());

                return ResponseEntity.created(url).header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
        }

        @GetMapping("/me")
        @PreAuthorize("hasRole('DRIVER')")
        public ResponseEntity<ApiResponse<?>> getMyProfile() {

                DriverResponseDTO driverResponseDTO = driverService.getDriverById();

                var response = ApiMapper.sucess(driverResponseDTO);

                return ResponseEntity.ok(response);
        }

        @PatchMapping("{driverId}/pix-key/register")
        @PreAuthorize("hasRole('DRIVER') && @driverSecurity.isOwner(#driverId)")
        public ResponseEntity<ApiResponse<?>> registerPixKey(@PathVariable UUID driverId,
                        @RequestBody @Valid PixKeyRegisterRequestDTO req) {

                driverService.registerPixKey(driverId, req);

                var response = ApiMapper.sucess(new MessageResponse("Pix key registered successfully"));

                return ResponseEntity.ok(response);
        }

}
