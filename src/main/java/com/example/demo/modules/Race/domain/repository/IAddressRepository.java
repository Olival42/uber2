package com.example.demo.modules.Race.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.modules.Race.domain.entity.AddressEntity;

@Repository
public interface IAddressRepository extends JpaRepository<AddressEntity, UUID> {}
