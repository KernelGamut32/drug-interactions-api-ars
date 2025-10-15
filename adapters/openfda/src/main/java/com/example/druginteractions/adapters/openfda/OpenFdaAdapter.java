package com.example.druginteractions.adapters.openfda;

import com.example.druginteractions.adapters.openfda.exception.OpenFdaException;
import com.example.druginteractions.adapters.openfda.exception.OpenFdaRateLimitException;
import com.example.druginteractions.domain.model.OpenFdaSignal;
import com.example.druginteractions.domain.ports.OpenFdaClient;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OpenFdaAdapter implements OpenFdaClient {
  private final WebClient openFdaWebClient;
  private static final String DRUG_EVENT_PATH = "/drug/event.json";

  @Override
  public Mono<OpenFdaSignal> fetchSignals(String drugA, String drugB, int limit) {
    String searchQuery = buildSearchQuery(drugA, drugB);
    String countField = "patient.reaction.reactionmeddrapt.exact";

    return openFdaWebClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(DRUG_EVENT_PATH)
                    .queryParam("search", searchQuery)
                    .queryParam("count", countField)
                    .queryParam("limit", limit)
                    .build())
        .retrieve()
        .bodyToMono(OpenFdaResponse.class)
        .map(this::mapToOpenFdaSignal)
        .onErrorResume(WebClientResponseException.class, this::handleWebClientError)
        .onErrorResume(Exception.class, this::handleGenericError);
  }

  private String buildSearchQuery(String drugA, String drugB) {
    return String.format(
        "patient.drug.medicinalproduct:\"%s\" AND patient.drug.medicinalproduct:\"%s\"",
        sanitizeDrugName(drugA), sanitizeDrugName(drugB));
  }

  private String sanitizeDrugName(String drugName) {
    return drugName.toUpperCase().trim();
  }

  private OpenFdaSignal mapToOpenFdaSignal(OpenFdaResponse response) {
    if (response.getResults() == null || response.getResults().isEmpty()) {
      return new OpenFdaSignal(0, Collections.emptyList());
    }

    long totalCount =
        response.getResults().stream().mapToInt(OpenFdaResponse.TermCount::getCount).sum();

    var topReactions =
        response.getResults().stream()
            .map(result -> Map.entry(result.getTerm(), (long) result.getCount()))
            .collect(Collectors.toList());

    return new OpenFdaSignal(totalCount, topReactions);
  }

  private Mono<OpenFdaSignal> handleWebClientError(WebClientResponseException ex) {
    if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
      return Mono.just(new OpenFdaSignal(0, Collections.emptyList()));
    }
    if (ex.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
      return Mono.error(
          new OpenFdaRateLimitException("Rate limit exceeded, please try again later"));
    }
    return Mono.error(new OpenFdaException("OpenFDA API error: " + ex.getMessage(), ex));
  }

  private Mono<OpenFdaSignal> handleGenericError(Throwable ex) {
    return Mono.error(new OpenFdaException("Error accessing OpenFDA API", ex));
  }
}
