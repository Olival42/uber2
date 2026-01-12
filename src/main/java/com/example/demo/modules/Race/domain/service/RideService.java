package com.example.demo.modules.Race.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.modules.Race.adapter.mapper.RideMapper;
import com.example.demo.modules.Race.adapter.mapper.RouteRequestMapper;
import com.example.demo.modules.Race.application.web.dto.RegisterRideDTO;
import com.example.demo.modules.Race.application.web.dto.ResponseRideDTO;
import com.example.demo.modules.Race.application.web.dto.RouteInfo;
import com.example.demo.modules.Race.application.web.dto.RouteRequestDTO;
import com.example.demo.modules.Race.domain.entity.AddressEntity;
import com.example.demo.modules.Race.domain.entity.RideEntity;
import com.example.demo.modules.Race.domain.repository.IRideRepository;
import com.example.demo.modules.User.domain.entity.PassengerEntity;
import com.example.demo.modules.User.domain.repository.IPassengerRepository;

import jakarta.transaction.Transactional;

@Service
public class RideService {

        @Autowired
        private IRideRepository rideRepository;

        @Autowired
        private IPassengerRepository passengerRepository;

        @Autowired
        private RideMapper rideMapper;

        @Autowired
        private RouteRequestMapper routeRequestMapper;

        @Autowired
        private DistanceService distanceService;

        @Autowired
        private AddressService addressService;

        @Autowired
        private PriceService priceService;

        @Transactional
        public ResponseRideDTO registerRide(RegisterRideDTO req) {

                AddressEntity pickupAddress = addressService.registerAddress(req.getPickupAddress());
                AddressEntity destinationAddress = addressService.registerAddress(req.getDestinationAddress());

                PassengerEntity passenger = passengerRepository.findById(req.getPassengerId())
                                .orElseThrow(() -> new RuntimeException("Passenger not found"));

                RouteRequestDTO routeRequest = routeRequestMapper.fromAddresses(pickupAddress, destinationAddress);

                RouteInfo route = distanceService.calculate(routeRequest);

                Double price = priceService.calculatePrice(route);

                RideEntity rideEntity = rideMapper.toEntity(passenger, pickupAddress, destinationAddress, price, route);

                var savedRideEntity = rideRepository.save(rideEntity);

                return rideMapper.toResponseDTO(savedRideEntity);
        }
}
