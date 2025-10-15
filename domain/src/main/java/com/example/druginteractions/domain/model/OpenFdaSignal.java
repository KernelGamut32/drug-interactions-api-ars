package com.example.druginteractions.domain.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public record OpenFdaSignal(
    @Min(value = 0, message = "Count must be non-negative") long count,
    @NotNull(message = "Top reactions list cannot be null")
        List<Map.Entry<String, Long>> topReactions) {}
