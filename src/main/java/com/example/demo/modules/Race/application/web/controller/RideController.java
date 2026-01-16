package com.example.demo.modules.Race.application.web.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.modules.Race.application.web.dto.AcceptRideDTO;
import com.example.demo.modules.Race.application.web.dto.CancelRideDTO;
import com.example.demo.modules.Race.application.web.dto.RegisterRideDTO;
import com.example.demo.modules.Race.application.web.dto.ResponseRideDTO;
import com.example.demo.modules.Race.domain.enums.StatusRide;
import com.example.demo.modules.Race.domain.service.RideService;
import com.example.demo.shared.ApiResponse;
import com.example.demo.shared.MessageResponse;
import com.example.demo.shared.mapper.ApiMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rides")
@Validated
public class RideController {

        @Autowired
        private RideService rideService;

        @PostMapping("/request")
        @PreAuthorize("hasRole('PASSENGER')")
        public ResponseEntity<ApiResponse<?>> requestRide(@RequestBody @Valid RegisterRideDTO req,
                        UriComponentsBuilder uri) {
                ResponseRideDTO response = rideService.requestRide(req);

                var url = uri.path("/rides/{id}").buildAndExpand(response.getRideId()).toUri();

                var apiResponse = ApiMapper.sucess(response);

                return ResponseEntity.created(url).body(apiResponse);
        }

        @PatchMapping("/accept/{rideId}")
        @PreAuthorize("hasRole('DRIVER')")
        public ResponseEntity<ApiResponse<?>> acceptRide(@PathVariable UUID rideId,
                        @RequestBody @Valid AcceptRideDTO req) {

                ResponseRideDTO response = rideService.acceptRide(rideId, req);

                var apiResponse = ApiMapper.sucess(response);

                return ResponseEntity.ok(apiResponse);
        }

        @DeleteMapping("/cancel/{rideId}")
        @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
        public ResponseEntity<ApiResponse<?>> cancelRide(@PathVariable UUID rideId,
                        @RequestBody @Valid CancelRideDTO req) {

                rideService.cancelRide(rideId, req);

                var apiResponse = ApiMapper.sucess(new MessageResponse("Ride cancelled successfully"));

                return ResponseEntity.ok(apiResponse);
        }

        @PreAuthorize("""
                                (hasRole('PASSENGER') && @rideSecurity.isPassengerOwner(#rideId)) ||
                                (hasRole('DRIVER') && @rideSecurity.isDriverOwner(#rideId))
                        """)
        @GetMapping("/{rideId}")
        public ResponseEntity<ApiResponse<?>> getRide(@PathVariable UUID rideId) {
                ResponseRideDTO response = rideService.getRideById(rideId);

                var apiResponse = ApiMapper.sucess(response);

                return ResponseEntity.ok(apiResponse);
        }

        @PatchMapping("/init/{rideId}")
        @PreAuthorize("hasRole('DRIVER') && @rideSecurity.isDriverOwner(#rideId)")
        public ResponseEntity<ApiResponse<?>> initRide(@PathVariable UUID rideId) {

                ResponseRideDTO response = rideService.initRide(rideId);

                var apiResponse = ApiMapper.sucess(response);

                return ResponseEntity.ok(apiResponse);
        }

        @PatchMapping("/complete/{rideId}")
        @PreAuthorize("hasRole('DRIVER') && @rideSecurity.isDriverOwner(#rideId)")
        public ResponseEntity<ApiResponse<?>> completeRide(@PathVariable UUID rideId) {

                ResponseRideDTO response = rideService.completeRide(rideId);

                var apiResponse = ApiMapper.sucess(response);

                return ResponseEntity.ok(apiResponse);
        }

        @GetMapping("/passenger/{passengerId}")
        @PreAuthorize("hasRole('PASSENGER') && @passengerSecurity.isOwner(#passengerId)")
        public ResponseEntity<ApiResponse<?>> getRidesByPassengerId(
                        @PathVariable UUID passengerId,
                        @RequestParam(required = false) UUID driverId,
                        @RequestParam(required = false) List<StatusRide> status,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "25") int size) {

                var response = rideService.getRidesByPassengerId(passengerId, driverId, status, page, size);

                var apiResponse = ApiMapper.sucess(response);

                return ResponseEntity.ok(apiResponse);

        }

        @GetMapping("/driver/{driverId}")
        @PreAuthorize("hasRole('DRIVER') && @driverSecurity.isOwner(#driverId)")
        public ResponseEntity<ApiResponse<?>> getRidesByDriverId(
                        @PathVariable UUID driverId,
                        @RequestParam(required = false) List<StatusRide> status,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "25") int size) {

                var response = rideService.getRidesByDriverId(driverId, status, page, size);

                var apiResponse = ApiMapper.sucess(response);

                return ResponseEntity.ok(apiResponse);

        }
}