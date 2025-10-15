package com.example.druginteractions.domain.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DrugInteraction {
  @Pattern(regexp = "^[A-Za-z][A-Za-z\\s-]{1,58}[A-Za-z]$")
  @Size(min = 3, max = 60)
  String drugA;

  @Pattern(regexp = "^[A-Za-z][A-Za-z\\s-]{1,58}[A-Za-z]$")
  @Size(min = 3, max = 60)
  String drugB;

  String note;
}
