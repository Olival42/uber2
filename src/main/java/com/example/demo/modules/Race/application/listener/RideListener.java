package com.example.demo.modules.Race.application.listener;

import java.time.Clock;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.demo.modules.Race.domain.enums.StatusRide;
import com.example.demo.modules.Race.domain.event.PromoteNextRideEvent;
import com.example.demo.modules.Race.domain.repository.IRideRepository;

@Component
public class RideListener {

    @Autowired
    private IRideRepository rideRepository;

    @Autowired
    private Clock clock;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(PromoteNextRideEvent event) {
        rideRepository
                .findFirstByDriverIdAndStatusOrderByRequestedAtAsc(event.driverId(),
                        StatusRide.ACCEPTED_PENDING)
                .ifPresent(nextRide -> {
                    nextRide.setStatus(StatusRide.ACCEPTED);
                    nextRide.setAcceptedAt(Instant.now(clock));

                    rideRepository.save(nextRide);
                });
    }
}
