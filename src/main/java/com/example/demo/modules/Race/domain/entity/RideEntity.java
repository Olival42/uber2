package com.example.demo.modules.Race.domain.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import com.example.demo.modules.Race.domain.enums.StatusRide;
import com.example.demo.modules.User.domain.entity.DriverEntity;
import com.example.demo.modules.User.domain.entity.PassengerEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rides")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RideEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = true)
    private DriverEntity driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id")
    private PassengerEntity passenger;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusRide status = StatusRide.REQUESTED;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_address_id")
    private AddressEntity pickupAddress;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_address_id")
    private AddressEntity destinationAddress;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double distanceKm;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Builder.Default
    private Instant requestedAt = Instant.now();

    @Column(nullable = true)
    private Instant startedAt;

    @Column(nullable = true)
    private Instant acceptedAt;

    @Column(nullable = true)
    private Instant finishedAt;

    @Column(nullable = true)
    private Instant cancelledAt;

    @Column(nullable = true)
    private String cancellationReason;

    public boolean isLateCancellation(Duration limit, Instant now) {
        return acceptedAt != null &&
                acceptedAt.plus(limit).isBefore(now);
    }
}