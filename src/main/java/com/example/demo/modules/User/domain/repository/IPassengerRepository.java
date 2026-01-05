package com.example.demo.modules.User.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.modules.User.domain.entity.PassengerEntity;

public interface IPassengerRepository extends JpaRepository<PassengerEntity, UUID>{

}
