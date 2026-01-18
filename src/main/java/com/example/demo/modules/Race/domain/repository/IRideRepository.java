package com.example.demo.modules.Race.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.modules.Race.domain.entity.RideEntity;
import com.example.demo.modules.Race.domain.enums.StatusRide;

import jakarta.persistence.LockModeType;

@Repository
public interface IRideRepository extends JpaRepository<RideEntity, UUID>, JpaSpecificationExecutor<RideEntity> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM RideEntity r WHERE r.id = :id")
    Optional<RideEntity> findByIdWithLock(UUID id);

    @Query("SELECT r FROM RideEntity r WHERE r.id = :rideId AND r.passenger.id = :passengerId")
    Optional<RideEntity> findByIdAndPassengerId(UUID rideId, UUID passengerId);

    @Query("SELECT r FROM RideEntity r WHERE r.id = :rideId AND r.driver.id = :passengerId")
    Optional<RideEntity> findByIdAndDriverId(UUID rideId, UUID passengerId);

    @Query("SELECT r FROM RideEntity r WHERE r.driver.id = :driverId AND r.status = :status ORDER BY r.requestedAt ASC")
    Optional<RideEntity> findFirstByDriverIdAndStatusOrderByRequestedAtAsc(UUID driverId, StatusRide status);

    Optional<RideEntity> findFirstByDriverIdOrderByFinishedAtDesc(UUID driverId);

    @Query("SELECT COUNT(r) FROM RideEntity r WHERE r.driver.id = :driverId AND r.status IN :status")
    long countByDriverIdAndStatusIn(UUID driverId, List<StatusRide> status);

    @Query("SELECT COUNT(r) > 0 FROM RideEntity r WHERE r.driver.id = :driverId AND r.status = :status")
    boolean existsByDriverIdAndStatus(UUID driverId, StatusRide status);

    @Query("SELECT COUNT(r) > 0 FROM RideEntity r WHERE r.passenger.id = :passengerId AND r.status IN :status")
    boolean existsByPassengerIdAndStatusIn(UUID passengerId, List<StatusRide> status);
}
