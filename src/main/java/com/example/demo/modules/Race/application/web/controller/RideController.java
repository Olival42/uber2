package com.example.demo.modules.Race.application.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.modules.Race.application.web.dto.RegisterRideDTO;
import com.example.demo.modules.Race.application.web.dto.ResponseRideDTO;
import com.example.demo.modules.Race.domain.service.RideService;
import com.example.demo.shared.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rides")
public class RideController {

    @Autowired
    private RideService rideService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<ApiResponse<?>> registerRide(@RequestBody @Valid RegisterRideDTO req,
            UriComponentsBuilder uri) {
        ResponseRideDTO response = rideService.registerRide(req);

        var url = uri.path("/rides/{id}").buildAndExpand(response.getRideId()).toUri();

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(true)
                .data(response)
                .error(null)
                .build();

        return ResponseEntity.created(url).body(apiResponse);
    }
}
