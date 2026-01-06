package com.example.demo.modules.User.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.modules.User.domain.entity.PassengerEntity;

@Repository
public interface IPassengerRepository extends JpaRepository<PassengerEntity, UUID>{
    Optional<PassengerEntity> findByEmail(String email);
}
