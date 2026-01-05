package com.example.demo.modules.User.application.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponseDTO {

    private UserResponseDTO user;
    private AuthTokenDTO tokens;
}
