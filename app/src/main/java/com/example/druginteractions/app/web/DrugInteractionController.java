package com.example.druginteractions.app.web;

import com.example.druginteractions.app.validation.ValidDrugName;
import com.example.druginteractions.app.web.dto.DrugInteractionRequest;
import com.example.druginteractions.app.web.dto.DrugInteractionResponse;
import com.example.druginteractions.app.web.mapper.DrugInteractionMapper;
import com.example.druginteractions.domain.model.DrugPair;
import com.example.druginteractions.domain.model.OpenFdaSignal;
import com.example.druginteractions.domain.service.DrugInteractionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class DrugInteractionController {
  private final DrugInteractionService service;
  private final DrugInteractionMapper mapper;

  @PostMapping("/interactions")
  public ResponseEntity<DrugInteractionResponse> saveNote(
      @Valid @RequestBody DrugInteractionRequest request) {
    DrugPair pair = mapper.toDrugPair(request.drugA(), request.drugB());
    return ResponseEntity.ok(mapper.toResponse(service.saveNote(pair, request.note())));
  }

  @GetMapping("/interactions")
  public ResponseEntity<DrugInteractionResponse> findNote(
      @ValidDrugName @RequestParam String drugA, @ValidDrugName @RequestParam String drugB) {
    DrugPair pair = mapper.toDrugPair(drugA, drugB);
    return service
        .findNote(pair)
        .map(note -> ResponseEntity.ok(mapper.toResponse(note)))
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/interactions/signals")
  public Mono<OpenFdaSignal> getSignals(
      @ValidDrugName @RequestParam String drugA, @ValidDrugName @RequestParam String drugB) {
    DrugPair pair = mapper.toDrugPair(drugA, drugB);
    return service.getSignals(pair);
  }
}
