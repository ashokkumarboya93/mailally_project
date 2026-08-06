package com.mailally.exception;

import com.mailally.common.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler capturing application-wide exceptions, logging them to console,
 * and formatting them into standard {@link ApiResponse}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
        log.error("CustomException encountered: {}", ex.getMessage());
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            log.error("Validation error - Field: '{}', Message: '{}'", fieldName, errorMessage);
        });

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .success(false)
                .message("Validation failed for input arguments")
                .data(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Malformed JSON or Deserialization error: ", ex);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Invalid request payload structure or JSON formatting")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(Exception ex) {
        log.warn("Authentication failure: {}", ex.getMessage());
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("Invalid email or password")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex) {
        log.error("Unhandled Exception caught: ", ex);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message("An unexpected error occurred: " + ex.getMessage())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


