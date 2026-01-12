package com.example.demo.modules.User.application.web.dto;

public record AuthResponseDTO (UserResponseDTO user, AuthTokenDTO tokens) {
}
