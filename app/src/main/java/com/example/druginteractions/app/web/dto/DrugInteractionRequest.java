package com.example.druginteractions.app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DrugInteractionRequest(
    @NotBlank(message = "Drug A name cannot be blank")
        @Pattern(
            regexp = "^[A-Za-z][A-Za-z\\s-]{1,58}[A-Za-z]$",
            message = "Drug A name must contain only letters, spaces, and hyphens")
        @Size(min = 3, max = 60, message = "Drug A name must be between 3 and 60 characters")
        String drugA,
    @NotBlank(message = "Drug B name cannot be blank")
        @Pattern(
            regexp = "^[A-Za-z][A-Za-z\\s-]{1,58}[A-Za-z]$",
            message = "Drug B name must contain only letters, spaces, and hyphens")
        @Size(min = 3, max = 60, message = "Drug B name must be between 3 and 60 characters")
        String drugB,
    @NotBlank(message = "Note cannot be blank")
        @Size(max = 1000, message = "Note must not exceed 1000 characters")
        String note) {}
