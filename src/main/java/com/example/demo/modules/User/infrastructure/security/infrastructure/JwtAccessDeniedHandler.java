package com.example.demo.modules.User.infrastructure.security.infrastructure;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.demo.shared.ApiResponse;
import com.example.demo.shared.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.access.AccessDeniedException accessDeniedException)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("FORBIDDEN")
                .message("Access Denied: " + accessDeniedException.getMessage())
                .build();

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(false)
                .data(null)
                .error(errorResponse)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

}
