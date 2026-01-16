package com.example.demo.modules.User.infrastructure.security.infrastructure;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.demo.shared.ErrorResponse;
import com.example.demo.shared.mapper.ApiMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Autowired
        private ObjectMapper objectMapper;

        @Override
        public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException authException)
                        throws IOException {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");

                ErrorResponse errorResponse = new ErrorResponse("UNAUTHORIZED",
                                "Unauthorized: " + authException.getMessage());

                var apiResponse = ApiMapper.error(errorResponse);

                response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        }

}
