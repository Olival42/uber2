package com.example.demo.modules.User.application.web.dto;

public record AuthTokenDTO (String accessToken, String refreshToken, Long expiresAt) {
}
