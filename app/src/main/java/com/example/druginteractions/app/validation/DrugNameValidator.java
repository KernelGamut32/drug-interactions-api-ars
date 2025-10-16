package com.example.druginteractions.app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class DrugNameValidator implements ConstraintValidator<ValidDrugName, String> {
  private static final Pattern DRUG_NAME_PATTERN =
      Pattern.compile("^[A-Za-z][A-Za-z\\s-]{1,58}[A-Za-z]$");

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }
    return DRUG_NAME_PATTERN.matcher(value).matches();
  }
}
