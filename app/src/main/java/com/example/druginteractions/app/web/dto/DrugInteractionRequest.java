package com.example.druginteractions.app.web.dto;

import com.example.druginteractions.app.validation.ValidDrugName;
import jakarta.validation.constraints.NotBlank;

public record DrugInteractionRequest(
    @ValidDrugName String drugA,
    @ValidDrugName String drugB,
    @NotBlank(message = "Note cannot be empty") String note) {}
