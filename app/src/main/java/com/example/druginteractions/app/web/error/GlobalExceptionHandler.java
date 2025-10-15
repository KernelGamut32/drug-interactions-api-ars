package com.example.druginteractions.app.web.error;

import com.example.druginteractions.adapters.openfda.exception.OpenFdaException;
import com.example.druginteractions.adapters.openfda.exception.OpenFdaRateLimitException;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations()
        .forEach(
            violation -> {
              String fieldName = violation.getPropertyPath().toString();
              String errorMessage = violation.getMessage();
              errors.put(fieldName, errorMessage);
            });

    return ResponseEntity.badRequest()
        .body(
            new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), "Validation failed", errors, Instant.now()));
  }

  @ExceptionHandler(OpenFdaRateLimitException.class)
  public ResponseEntity<ErrorResponse> handleRateLimitException(OpenFdaRateLimitException ex) {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
        .body(
            new ErrorResponse(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "OpenFDA rate limit exceeded",
                Map.of("message", ex.getMessage()),
                Instant.now()));
  }

  @ExceptionHandler(OpenFdaException.class)
  public ResponseEntity<ErrorResponse> handleOpenFdaException(OpenFdaException ex) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
        .body(
            new ErrorResponse(
                HttpStatus.BAD_GATEWAY.value(),
                "OpenFDA service error",
                Map.of("message", ex.getMessage()),
                Instant.now()));
  }

  @ExceptionHandler(ResourceAccessException.class)
  public ResponseEntity<ErrorResponse> handleResourceAccessException(ResourceAccessException ex) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
        .body(
            new ErrorResponse(
                HttpStatus.BAD_GATEWAY.value(),
                "External service error",
                Map.of("message", "Unable to access external service"),
                Instant.now()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    return ResponseEntity.internalServerError()
        .body(
            new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                Map.of("message", "An unexpected error occurred"),
                Instant.now()));
  }

  // Public record for error responses
  public record ErrorResponse(
      int status, String message, Map<String, String> errors, Instant timestamp) {}
}
