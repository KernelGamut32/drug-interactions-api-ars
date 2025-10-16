package com.example.druginteractions.app.web.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ProblemDetail;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValidationProblemDetails extends ProblemDetail {
  private List<ValidationError> errors = new ArrayList<>();

  public ValidationProblemDetails(String title) {
    super(422);
    setTitle(title);
  }

  public List<ValidationError> getErrors() {
    return errors;
  }

  public void addError(String field, String message) {
    errors.add(new ValidationError(field, message));
  }

  record ValidationError(String field, String message) {}
}
