package com.example.druginteractions.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record InteractionNote(
    @NotNull(message = "ID cannot be null") UUID id,
    @NotNull(message = "Drug pair cannot be null") DrugPair pair,
    @NotBlank(message = "Note cannot be blank") String note,
    @NotNull(message = "Updated timestamp cannot be null") Instant updatedAt) {
  public InteractionNote {
    if (id == null) {
      id = UUID.randomUUID();
    }
    if (updatedAt == null) {
      updatedAt = Instant.now();
    }
  }
}
