package com.example.druginteractions.app.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DrugNameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDrugName {
  String message() default
      "Drug name must be 3-60 characters long and contain only letters, spaces, and hyphens";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
