package com.example.demo.modules.User.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.modules.User.domain.entity.UserEntity;

public interface IUserRepository extends JpaRepository<UserEntity, UUID>{
    Optional<UserEntity> findByEmail(String email);
}
