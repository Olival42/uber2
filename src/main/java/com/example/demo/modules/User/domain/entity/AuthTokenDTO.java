package com.example.demo.modules.User.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthTokenDTO {

    private String accessToken;
    private Long expiresAt;
    private String refreshToken;
}
