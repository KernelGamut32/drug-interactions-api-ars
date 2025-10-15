package com.example.druginteractions.adapters.openfda.exception;

public class OpenFdaRateLimitException extends RuntimeException {
  public OpenFdaRateLimitException(String message) {
    super(message);
  }
}
