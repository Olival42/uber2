package com.example.demo.modules.Race.domain.service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.infrastructure.exception.BusinessRuleException;
import com.example.demo.modules.Race.adapter.mapper.RideMapper;
import com.example.demo.modules.Race.adapter.mapper.RouteRequestMapper;
import com.example.demo.modules.Race.application.web.dto.RouteInfo;
import com.example.demo.modules.Race.application.web.dto.request.AcceptRideRequestDTO;
import com.example.demo.modules.Race.application.web.dto.request.CancelRideRequestDTO;
import com.example.demo.modules.Race.application.web.dto.request.RegisterRideRequestDTO;
import com.example.demo.modules.Race.application.web.dto.request.RouteRequestDTO;
import com.example.demo.modules.Race.application.web.dto.response.ResponseRideDTO;
import com.example.demo.modules.Race.domain.entity.AddressEntity;
import com.example.demo.modules.Race.domain.entity.RideEntity;
import com.example.demo.modules.Race.domain.enums.StatusRide;
import com.example.demo.modules.Race.domain.event.PromoteNextRideEvent;
import com.example.demo.modules.Race.domain.repository.IRideRepository;
import com.example.demo.modules.Race.infrastructure.specification.RideSpecification;
import com.example.demo.modules.User.domain.entity.DriverEntity;
import com.example.demo.modules.User.domain.entity.PassengerEntity;
import com.example.demo.modules.User.domain.repository.IDriverRepository;
import com.example.demo.modules.User.domain.repository.IPassengerRepository;
import com.example.demo.shared.PageResponse;
import com.example.demo.shared.mapper.PageMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RideService {

        private static final int MAX_ACTIVE_RIDES = 2;

        private static final List<StatusRide> DRIVER_ACTIVE_STATUSES = List.of(StatusRide.ACCEPTED,
                        StatusRide.ACCEPTED_PENDING, StatusRide.IN_PROGRESS);

        private static final List<StatusRide> PASSENGER_ACTIVE_STATUSES = List.of(StatusRide.REQUESTED,
                        StatusRide.ACCEPTED, StatusRide.ACCEPTED_PENDING);

        private static final EnumSet<StatusRide> NON_CANCELLABLE_STATUSES = EnumSet.of(StatusRide.CANCELLED,
                        StatusRide.IN_PROGRESS, StatusRide.COMPLETED);

        private static final Duration FREE_CANCELLATION_WINDOW = Duration.ofMinutes(5);

        @Autowired
        private IRideRepository rideRepository;

        @Autowired
        private IPassengerRepository passengerRepository;

        @Autowired
        private IDriverRepository driverRepository;

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

        @Autowired
        private ApplicationEventPublisher applicationEventPublisher;

        @Autowired
        private Clock clock;

        @Transactional
        public ResponseRideDTO requestRide(RegisterRideRequestDTO req) {

                PassengerEntity passenger = passengerRepository.findById(req.getPassengerId())
                                .orElseThrow(() -> new EntityNotFoundException("Passenger not found"));

                if (rideRepository.existsByPassengerIdAndStatusIn(passenger.getId(), PASSENGER_ACTIVE_STATUSES)) {
                        throw new BusinessRuleException("Passenger already has an active ride");
                }

                AddressEntity pickupAddress = addressService.registerAddress(req.getPickupAddress());
                AddressEntity destinationAddress = addressService.registerAddress(req.getDestinationAddress());

                RouteRequestDTO routeRequest = routeRequestMapper.fromAddresses(pickupAddress, destinationAddress);

                RouteInfo route = distanceService.calculate(routeRequest);

                Double price = priceService.calculatePrice(route);

                RideEntity rideEntity = rideMapper.toEntity(passenger, pickupAddress, destinationAddress, price, route);

                return rideMapper.toResponseDTO(rideRepository.save(rideEntity));
        }

        @Transactional
        public ResponseRideDTO acceptRide(UUID rideId, AcceptRideRequestDTO req) {

                long count = rideRepository.countByDriverIdAndStatusIn(req.getDriverId(), DRIVER_ACTIVE_STATUSES);

                if (count >= MAX_ACTIVE_RIDES) {
                        throw new BusinessRuleException("Driver already has an active ride and a pending ride");
                }

                RideEntity rideEntity = rideRepository.findByIdWithLock(rideId)
                                .orElseThrow(() -> new EntityNotFoundException("Ride not found"));

                if (rideEntity.getStatus() != StatusRide.REQUESTED) {
                        throw new BusinessRuleException("Ride is not in a state to be accepted");
                }

                DriverEntity driverEntity = driverRepository.findById(req.getDriverId())
                                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

                rideEntity.setDriver(driverEntity);

                if (rideRepository.existsByDriverIdAndStatus(req.getDriverId(), StatusRide.ACCEPTED))
                        rideEntity.setStatus(StatusRide.ACCEPTED_PENDING);
                else {
                        rideEntity.setStatus(StatusRide.ACCEPTED);
                        rideEntity.setAcceptedAt(Instant.now(clock));
                }

                return rideMapper.toResponseDTO(rideRepository.save(rideEntity));
        }

        @Transactional
        public void cancelRide(UUID rideId, CancelRideRequestDTO req) {

                RideEntity rideEntity = rideRepository.findByIdWithLock(rideId)
                                .orElseThrow(() -> new EntityNotFoundException("Ride not found"));

                if (NON_CANCELLABLE_STATUSES.contains(rideEntity.getStatus())) {
                        throw new BusinessRuleException("Ride cannot be cancelled in its current status");
                }

                if (rideEntity.getStatus() == StatusRide.ACCEPTED
                                && rideEntity.isLateCancellation(FREE_CANCELLATION_WINDOW, Instant.now(clock))) {
                        throw new BusinessRuleException("Cancellation after 5 minutes requires a fee");
                }

                // Criar lógica para multa/penalizção

                StatusRide previousStatus = rideEntity.getStatus();
                UUID driverId = rideEntity.getDriver() != null ? rideEntity.getDriver().getId() : null;

                rideEntity.setStatus(StatusRide.CANCELLED);
                rideEntity.setCancelledAt(Instant.now(clock));
                rideEntity.setCancellationReason(req.getCancellationReason());

                rideRepository.save(rideEntity);

                if (previousStatus == StatusRide.ACCEPTED && driverId != null) {
                        applicationEventPublisher.publishEvent(new PromoteNextRideEvent(driverId));
                }
        }

        @Transactional
        public ResponseRideDTO initRide(UUID rideId) {
                RideEntity rideEntity = rideRepository.findByIdWithLock(rideId)
                                .orElseThrow(() -> new EntityNotFoundException("Ride not found"));

                if (rideEntity.getStatus() != StatusRide.ACCEPTED) {
                        throw new BusinessRuleException("Ride is not in a state to be initialized");
                }

                DriverEntity driver = Optional.ofNullable(rideEntity.getDriver())
                                .orElseThrow(() -> new BusinessRuleException("Accepted ride must have a driver"));

                if (rideRepository.existsByDriverIdAndStatus(driver.getId(), StatusRide.IN_PROGRESS)) {
                        throw new BusinessRuleException("Driver already has an active ride");
                }

                rideEntity.setStatus(StatusRide.IN_PROGRESS);
                rideEntity.setStartedAt(Instant.now(clock));

                var savedEntity = rideRepository.save(rideEntity);

                applicationEventPublisher.publishEvent(new PromoteNextRideEvent(driver.getId()));

                return rideMapper.toResponseDTO(savedEntity);
        }

        @Transactional
        public ResponseRideDTO completeRide(UUID rideId) {
                RideEntity rideEntity = rideRepository.findByIdWithLock(rideId)
                                .orElseThrow(() -> new EntityNotFoundException("Ride not found"));

                if (rideEntity.getStatus() != StatusRide.IN_PROGRESS) {
                        throw new BusinessRuleException("Ride is not in a state to be completed");
                }

                DriverEntity driver = Optional.ofNullable(rideEntity.getDriver())
                                .orElseThrow(() -> new BusinessRuleException("Ride in progress must have a driver"));

                rideEntity.setStatus(StatusRide.COMPLETED);
                rideEntity.setFinishedAt(Instant.now(clock));

                var savedEntity = rideRepository.save(rideEntity);

                applicationEventPublisher.publishEvent(new PromoteNextRideEvent(driver.getId()));

                return rideMapper.toResponseDTO(savedEntity);
        }

        @Transactional(readOnly = true)
        public PageResponse<?> getRidesByPassengerId(
                        UUID passengerId,
                        UUID driverId,
                        List<StatusRide> listStatus,
                        int page,
                        int size) {

                int safePage = Math.max(page, 0);
                int safeSize = Math.min(size, 20);

                Specification<RideEntity> spec = RideSpecification.base()
                                .and(RideSpecification.withPassengerId(passengerId));

                if (listStatus != null && !listStatus.isEmpty()) {
                        spec = spec.and(RideSpecification.withStatusIn(listStatus));
                }

                Pageable pageResult = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.ASC, "requestedAt"));

                return PageMapper.toPageResponse(rideRepository.findAll(spec, pageResult)
                                .map(rideMapper::toResponseDTO));
        }

        @Transactional(readOnly = true)
        public PageResponse<?> getRidesByDriverId(
                        UUID driverId,
                        List<StatusRide> listStatus,
                        int page,
                        int size) {

                int safePage = Math.max(page, 0);
                int safeSize = Math.min(size, 20);

                Specification<RideEntity> spec = RideSpecification.base()
                                .and(RideSpecification.withDriverId(driverId));

                if (listStatus != null && !listStatus.isEmpty()) {
                        spec = spec.and(RideSpecification.withStatusIn(listStatus));
                }

                Pageable pageResult = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.ASC, "requestedAt"));

                return PageMapper.toPageResponse(rideRepository.findAll(spec, pageResult)
                                .map(rideMapper::toResponseDTO));
        }

        @Transactional(readOnly = true)
        public ResponseRideDTO getRideById(UUID rideId) {

                RideEntity rideEntity = rideRepository.findById(rideId)
                                .orElseThrow(() -> new EntityNotFoundException("Ride not found"));

                if (rideEntity.getStatus() == StatusRide.CANCELLED) {
                        throw new AccessDeniedException("Cancelled rides are restricted");
                }

                return rideMapper.toResponseDTO(rideEntity);
        }
}
