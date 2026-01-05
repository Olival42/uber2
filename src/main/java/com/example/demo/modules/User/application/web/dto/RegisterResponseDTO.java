package com.example.demo.modules.User.application.web.dto;

import com.example.demo.modules.User.domain.entity.AuthTokenDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterResponseDTO {

    private UserResponseDTO user;
    private AuthTokenDTO tokens;
}
