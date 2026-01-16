package com.example.demo.modules.Race.infrastructure.specification;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.modules.Race.domain.entity.RideEntity;
import com.example.demo.modules.Race.domain.enums.StatusRide;

public final class RideSpecification {

    public static Specification<RideEntity> base() {
        return statusNotCancelled();
    }

    public static Specification<RideEntity> withPassengerId(UUID passengerId) {
        return (root, query, cb) -> {
            if (passengerId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.join("passenger").get("id"), passengerId);
        };
    }

    public static Specification<RideEntity> withDriverId(UUID driverId) {
        return (root, query, cb) -> {
            if (driverId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.join("driver").get("id"), driverId);
        };
    }

    public static Specification<RideEntity> withStatusIn(List<StatusRide> statuses) {
        return (root, query, cb) -> root.get("status").in(statuses);
    }

    public static Specification<RideEntity> statusNotCancelled() {
        return (root, query, cb) -> cb.notEqual(root.get("status"), StatusRide.CANCELLED);
    }
}
