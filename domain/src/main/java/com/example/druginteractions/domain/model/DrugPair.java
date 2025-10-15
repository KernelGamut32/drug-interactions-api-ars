package com.example.druginteractions.domain.model;

import jakarta.validation.constraints.NotBlank;

public record DrugPair(
    @NotBlank(message = "Drug A name cannot be blank") String drugA,
    @NotBlank(message = "Drug B name cannot be blank") String drugB) {
  public DrugPair {
    // Normalize drug names by trimming and converting to uppercase
    drugA = normalizeDrugName(drugA);
    drugB = normalizeDrugName(drugB);

    // Ensure consistent ordering of drug pairs
    if (drugA.compareTo(drugB) > 0) {
      String temp = drugA;
      drugA = drugB;
      drugB = temp;
    }
  }

  private static String normalizeDrugName(String name) {
    return name != null ? name.trim().toUpperCase() : "";
  }
}
