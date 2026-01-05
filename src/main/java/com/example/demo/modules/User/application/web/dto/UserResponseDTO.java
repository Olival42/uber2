package com.example.demo.modules.User.application.web.dto;

import java.time.Instant;

import com.example.demo.modules.User.domain.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private String id;
    private String email;
    private String name;
    private UserRole role;
    private Boolean active;
    private Instant createdAt;
}
