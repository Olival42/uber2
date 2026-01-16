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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.demo.infrastructure.exception.security.InsufficientAuthenticationException;
import com.example.demo.infrastructure.exception.security.InvalidTokenException;
import com.example.demo.infrastructure.exception.security.InvalidTokenTypeException;
import com.example.demo.infrastructure.exception.security.MissingTokenException;
import com.example.demo.infrastructure.exception.security.TokenExpiredException;
import com.example.demo.infrastructure.exception.security.TokenRevokedException;
import com.example.demo.shared.ApiResponse;
import com.example.demo.shared.ErrorResponse;
import com.example.demo.shared.mapper.ApiMapper;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ApiResponse<?>> handleEntityNotFound(EntityNotFoundException er) {

                ErrorResponse error = new ErrorResponse("ENTITY_NOT_FOUND", er.getMessage());

                var response = ApiMapper.error(error);

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
                ErrorResponse error = new ErrorResponse("ILLEGAL_STATE", er.getMessage());

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException er) {
                ErrorResponse error = new ErrorResponse("ILLEGAL_ARGUMENT", er.getMessage());

                var response = ApiMapper.error(error);

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

                ErrorResponse error = new ErrorResponse("CONFLICT", message);

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadable(HttpMessageNotReadableException er) {
                ErrorResponse error = new ErrorResponse("INVALID_BODY", "Request body is invalid or missing.");

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException er,
                        WebRequest request) {
                ErrorResponse error = new ErrorResponse("ACCESS_DENIED", er.getMessage());

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        @ExceptionHandler(JwtException.class)
        public ResponseEntity<ApiResponse<?>> handleJwtException(JwtException ex) {
                ErrorResponse error = new ErrorResponse("INVALID_TOKEN", "Token is invalid or malformed.");

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(TokenExpiredException.class)
        public ResponseEntity<ApiResponse<?>> handleTokenExpired(TokenExpiredException ex) {
                ErrorResponse error = new ErrorResponse("TOKEN_EXPIRED", ex.getMessage());

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(TokenRevokedException.class)
        public ResponseEntity<ApiResponse<?>> handleTokenRevoked(TokenRevokedException ex) {
                ErrorResponse error = new ErrorResponse("TOKEN_REVOKED", ex.getMessage());

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(MissingTokenException.class)
        public ResponseEntity<ApiResponse<?>> handleMissingToken(MissingTokenException ex) {
                ErrorResponse error = new ErrorResponse("MISSING_TOKEN", ex.getMessage());

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ApiResponse<?>> handleBadCredentials(BadCredentialsException ex) {
                ErrorResponse error = new ErrorResponse("BAD_CREDENTIALS", "Credentials invalid");

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(InvalidTokenTypeException.class)
        public ResponseEntity<ApiResponse<?>> handleInvalidTokenType(InvalidTokenTypeException ex) {
                ErrorResponse error = new ErrorResponse("INVALID_TOKEN_TYPE", ex.getMessage());

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(SecurityException.class)
        public ResponseEntity<ApiResponse<?>> handleSecurityException(SecurityException ex) {
                ErrorResponse error = new ErrorResponse("SECURITY_EXCEPTION", ex.getMessage());

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(InvalidTokenException.class)
        public ResponseEntity<ApiResponse<?>> handleInvalidToken(InvalidTokenException ex) {
                ErrorResponse error = new ErrorResponse("INVALID_TOKEN", ex.getMessage());

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(InsufficientAuthenticationException.class)
        public ResponseEntity<ApiResponse<?>> handleInsufficientAuthentication(InsufficientAuthenticationException ex) {
                ErrorResponse error = new ErrorResponse("INSUFFICIENT_AUTHENTICATION", ex.getMessage());

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(RouteNotFoundException.class)
        public ResponseEntity<ApiResponse<?>> handleRouteNotFound(RouteNotFoundException ex) {
                ErrorResponse error = new ErrorResponse("ROUTE_NOT_FOUND", ex.getMessage());

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        @ExceptionHandler(GeocodingNotFoundException.class)
        public ResponseEntity<ApiResponse<?>> handleGeocodingNotFound(GeocodingNotFoundException ex) {
                ErrorResponse error = new ErrorResponse("GEOCODING_NOT_FOUND", ex.getMessage());

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(response);
        }

        @ExceptionHandler(BusinessRuleException.class)
        public ResponseEntity<ApiResponse<?>> handleBusinessRule(BusinessRuleException ex) {
                ErrorResponse error = new ErrorResponse("BUSINESS_RULE_VIOLATION", ex.getMessage());

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<?>> handleInternalServerError(Exception ex) {
                ErrorResponse error = new ErrorResponse("INTERNAL_SERVER_ERROR", ex.getMessage());

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ApiResponse<?>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {

                String paramName = ex.getName();
                Object value = ex.getValue();
                // Class<?> requiredType = ex.getRequiredType();

                String message;

                message = String.format("Invalid value for parameter '%s'. Received '%s'.", paramName, value);

                ErrorResponse error = new ErrorResponse("INVALID_PARAMETER", message);

                var response = ApiMapper.error(error);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        private record DataErrors(String field, String message) {

                public DataErrors(FieldError error) {
                        this(error.getField(), error.getDefaultMessage());
                }
        }
}
