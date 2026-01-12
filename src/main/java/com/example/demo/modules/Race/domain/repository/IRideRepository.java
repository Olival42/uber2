package com.example.demo.modules.Race.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.modules.Race.domain.entity.RideEntity;

@Repository
public interface IRideRepository extends JpaRepository<RideEntity, UUID> {}
