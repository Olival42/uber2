package com.example.demo.modules.User.domain.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.modules.User.domain.entity.ResetPasswordTokenEntity;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface IResetPassowordRepository extends JpaRepository<ResetPasswordTokenEntity, Long> {
    void deleteByUserEmail(String userEmail);

    @Query("SELECT t FROM ResetPasswordTokenEntity t WHERE t.expiration > :now")
    List<ResetPasswordTokenEntity> findValidToken(@Param("now") Instant now);
}
