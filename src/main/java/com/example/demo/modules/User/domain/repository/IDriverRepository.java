package com.example.demo.modules.User.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.modules.User.domain.entity.DriverEntity;

@Repository
public interface IDriverRepository extends JpaRepository<DriverEntity, UUID>{

}
