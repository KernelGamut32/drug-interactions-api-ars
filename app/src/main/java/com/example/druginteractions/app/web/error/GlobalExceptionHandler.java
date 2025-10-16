package com.example.druginteractions.app.web.error;

import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(WebExchangeBindException.class)
  public ProblemDetail handleValidationErrors(WebExchangeBindException ex) {
    ValidationProblemDetails problem = new ValidationProblemDetails("Validation Failed");
    problem.setType(URI.create("https://api.druginteractions.example.com/errors/validation"));
    problem.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> problem.addError(error.getField(), error.getDefaultMessage()));

    return problem;
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
    ValidationProblemDetails problem = new ValidationProblemDetails("Validation Failed");
    problem.setType(URI.create("https://api.druginteractions.example.com/errors/validation"));
    problem.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());

    ex.getConstraintViolations()
        .forEach(
            violation ->
                problem.addError(violation.getPropertyPath().toString(), violation.getMessage()));

    return problem;
  }
}
