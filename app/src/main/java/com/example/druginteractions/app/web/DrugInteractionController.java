package com.example.druginteractions.app.web;

import com.example.druginteractions.app.web.dto.DrugInteractionRequest;
import com.example.druginteractions.app.web.dto.DrugInteractionResponse;
import com.example.druginteractions.app.web.mapper.DrugInteractionMapper;
import com.example.druginteractions.domain.model.DrugPair;
import com.example.druginteractions.domain.model.OpenFdaSignal;
import com.example.druginteractions.domain.service.DrugInteractionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
      @Pattern(regexp = "^[A-Za-z][A-Za-z\\s-]{1,58}[A-Za-z]$")
          @Size(min = 3, max = 60)
          @RequestParam
          String drugA,
      @Pattern(regexp = "^[A-Za-z][A-Za-z\\s-]{1,58}[A-Za-z]$")
          @Size(min = 3, max = 60)
          @RequestParam
          String drugB) {
    DrugPair pair = mapper.toDrugPair(drugA, drugB);
    return service
        .findNote(pair)
        .map(note -> ResponseEntity.ok(mapper.toResponse(note)))
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/signals")
  public Mono<ResponseEntity<OpenFdaSignal>> getSignals(
      @Pattern(regexp = "^[A-Za-z][A-Za-z\\s-]{1,58}[A-Za-z]$")
          @Size(min = 3, max = 60)
          @RequestParam
          String drugA,
      @Pattern(regexp = "^[A-Za-z][A-Za-z\\s-]{1,58}[A-Za-z]$")
          @Size(min = 3, max = 60)
          @RequestParam
          String drugB,
      @Min(1) @Max(100) @RequestParam(defaultValue = "50") int limit) {
    DrugPair pair = mapper.toDrugPair(drugA, drugB);
    return service
        .getSignals(pair, limit)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
