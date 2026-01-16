package com.example.demo.modules.User.infrastructure.security.infrastructure;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.demo.shared.ErrorResponse;
import com.example.demo.shared.mapper.ApiMapper;
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

                ErrorResponse errorResponse = new ErrorResponse("FORBIDDEN",
                                "Access Denied: " + accessDeniedException.getMessage());

                var apiResponse = ApiMapper.error(errorResponse);

                response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        }

}
