package com.example.demo.infrastructure.exception;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.infrastructure.exception.security.InsufficientAuthenticationException;
import com.example.demo.infrastructure.exception.security.InvalidTokenException;
import com.example.demo.infrastructure.exception.security.InvalidTokenTypeException;
import com.example.demo.infrastructure.exception.security.MissingTokenException;
import com.example.demo.infrastructure.exception.security.TokenExpiredException;
import com.example.demo.infrastructure.exception.security.TokenRevokedException;
import com.example.demo.shared.ApiResponse;
import com.example.demo.shared.ErrorResponse;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ApiResponse<?>> handleEntityNotFound(EntityNotFoundException er) {

                ErrorResponse error = ErrorResponse.builder()
                                .code("ENTITY_NOT_FOUND")
                                .message(er.getMessage())
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException er) {
                List<DataErrors> erros = er.getFieldErrors().stream()
                                .map(DataErrors::new)
                                .toList();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(erros)
                                .error(null)
                                .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<ApiResponse<?>> handleIllegalState(IllegalStateException er) {
                ErrorResponse error = ErrorResponse.builder()
                                .code("ILLEGAL_STATE")
                                .message(er.getMessage())
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException er) {
                ErrorResponse error = ErrorResponse.builder()
                                .code("ILLEGAL_ARGUMENT")
                                .message(er.getMessage())
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ApiResponse<?>> handleDataIntegrity(DataIntegrityViolationException er) {
                String message = "Data integrity error.";
                String rootCauseMessage = er.getMostSpecificCause() != null ? er.getMostSpecificCause().getMessage()
                                : er.getMessage();

                if (rootCauseMessage != null) {
                        if (rootCauseMessage.toLowerCase().contains("duplicate key")
                                        || rootCauseMessage.toLowerCase().contains("violates unique constraint")) {
                                message = "A user with the given email or username already exists.";
                        }
                }

                ErrorResponse error = ErrorResponse.builder()
                                .code("CONFLICT")
                                .message(message)
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();

                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadable(HttpMessageNotReadableException er) {
                ErrorResponse error = ErrorResponse.builder()
                                .code("INVALID_BODY")
                                .message("Request body is invalid or missing.")
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException er,
                        WebRequest request) {
                ErrorResponse error = ErrorResponse.builder()
                                .code("ACCESS_DENIED")
                                .message(er.getMessage())
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        @ExceptionHandler(JwtException.class)
        public ResponseEntity<ApiResponse<?>> handleJwtException(JwtException ex) {
                ErrorResponse error = ErrorResponse.builder()
                                .code("INVALID_TOKEN")
                                .message("Token is invalid or malformed.")
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(TokenExpiredException.class)
        public ResponseEntity<ApiResponse<?>> handleTokenExpired(TokenExpiredException ex) {
                ErrorResponse error = ErrorResponse.builder()
                                .code("TOKEN_EXPIRED")
                                .message(ex.getMessage())
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(TokenRevokedException.class)
        public ResponseEntity<ApiResponse<?>> handleTokenRevoked(TokenRevokedException ex) {
                ErrorResponse error = ErrorResponse.builder()
                                .code("TOKEN_REVOKED")
                                .message(ex.getMessage())
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(MissingTokenException.class)
        public ResponseEntity<ApiResponse<?>> handleMissingToken(MissingTokenException ex) {
                ErrorResponse error = ErrorResponse.builder()
                                .code("MISSING_TOKEN")
                                .message(ex.getMessage())
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ApiResponse<?>> handleBadCredentials(BadCredentialsException ex) {
                ErrorResponse error = ErrorResponse.builder()
                                .code("BAD_CREDENTIALS")
                                .message("Credentials invalid")
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(InvalidTokenTypeException.class)
        public ResponseEntity<ApiResponse<?>> handleInvalidTokenType(InvalidTokenTypeException ex) {
                ErrorResponse error = ErrorResponse.builder()
                                .code("INVALID_TOKEN_TYPE")
                                .message(ex.getMessage())
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(SecurityException.class)
        public ResponseEntity<ApiResponse<?>> handleSecurityException(SecurityException er) {
                ErrorResponse error = ErrorResponse.builder()
                                .code("SECURITY_EXCEPTION")
                                .message(er.getMessage())
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(InvalidTokenException.class)
        public ResponseEntity<ApiResponse<?>> handleInvalidToken(InvalidTokenException ex) {
                ErrorResponse error = ErrorResponse.builder()
                                .code("INVALID_TOKEN")
                                .message(ex.getMessage())
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(InsufficientAuthenticationException.class)
        public ResponseEntity<ApiResponse<?>> handleInsufficientAuthentication(InsufficientAuthenticationException ex) {
                ErrorResponse error = ErrorResponse.builder()
                                .code("INSUFFICIENT_AUTHENTICATION")
                                .message(ex.getMessage())
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(RouteNotFoundException.class)
        public ResponseEntity<ApiResponse<?>> handleRouteNotFound(RouteNotFoundException ex) {
                ErrorResponse error = ErrorResponse.builder()
                                .code("ROUTE_NOT_FOUND")
                                .message(ex.getMessage())
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<?>> handleInternalServerError(Exception er) {
                ErrorResponse error = ErrorResponse.builder()
                                .code("INTERNAL_SERVER_ERROR")
                                .message(er.getMessage())
                                .build();

                ApiResponse<?> response = ApiResponse.builder()
                                .success(false)
                                .data(null)
                                .error(error)
                                .build();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        private record DataErrors(String field, String message) {

                public DataErrors(FieldError error) {
                        this(error.getField(), error.getDefaultMessage());
                }
        }
}
