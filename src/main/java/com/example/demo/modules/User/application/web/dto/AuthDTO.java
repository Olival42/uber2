package com.example.demo.modules.User.application.web.dto;

import com.example.demo.modules.User.application.web.dto.response.UserResponseDTO;

public record AuthDTO (UserResponseDTO user, AuthTokenDTO tokens) {}
