package com.example.druginteractions.domain.service;

import com.example.druginteractions.domain.config.DrugInteractionProperties;
import com.example.druginteractions.domain.model.DrugPair;
import com.example.druginteractions.domain.model.InteractionNote;
import com.example.druginteractions.domain.model.OpenFdaSignal;
import com.example.druginteractions.domain.ports.DrugInteractionRepository;
import com.example.druginteractions.domain.ports.OpenFdaClient;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.time.Instant;
import java.util.Optional;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DrugInteractionService {
  private final DrugInteractionRepository repository;
  private final OpenFdaClient openFdaClient;
  private final Validator validator;
  private final DrugInteractionProperties properties;

  public DrugInteractionService(
      DrugInteractionRepository repository,
      OpenFdaClient openFdaClient,
      Validator validator,
      DrugInteractionProperties properties) {
    this.repository = repository;
    this.openFdaClient = openFdaClient;
    this.validator = validator;
    this.properties = properties;
  }

  public Optional<InteractionNote> findNote(@Valid DrugPair pair) {
    validateInput(pair);
    return repository.find(pair);
  }

  public InteractionNote saveNote(@Valid DrugPair pair, String note) {
    validateInput(pair);
    if (note == null || note.trim().isEmpty()) {
      throw new IllegalArgumentException("Note cannot be empty");
    }

    InteractionNote interactionNote =
        new InteractionNote(
            null, // Will be generated in the constructor
            pair,
            note.trim(),
            Instant.now());
    validateInput(interactionNote);
    return repository.upsert(interactionNote);
  }

  public Mono<OpenFdaSignal> getSignals(@Valid DrugPair pair) {
    return getSignals(pair, properties.getDefaultSignalLimit());
  }

  public Mono<OpenFdaSignal> getSignals(@Valid DrugPair pair, int limit) {
    validateInput(pair);
    if (limit <= 0) {
      throw new IllegalArgumentException("Limit must be positive");
    }
    return openFdaClient.fetchSignals(pair.drugA(), pair.drugB(), limit);
  }

  private void validateInput(Object input) {
    var violations = validator.validate(input);
    if (!violations.isEmpty()) {
      throw new IllegalArgumentException(
          "Validation failed: " + violations.iterator().next().getMessage());
    }
  }
}
