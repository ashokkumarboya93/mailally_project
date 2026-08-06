package com.mailally.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mailally.common.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * AuthenticationEntryPoint returning 401 Unauthorized JSON response on unauthenticated access attempts.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        if (objectMapper != null) {
            this.objectMapper = objectMapper;
        } else {
            this.objectMapper = new ObjectMapper();
            this.objectMapper.registerModule(new JavaTimeModule());
        }
    }

    public CustomAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .success(false)
                .message("Unauthorized: " + authException.getMessage())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
}

