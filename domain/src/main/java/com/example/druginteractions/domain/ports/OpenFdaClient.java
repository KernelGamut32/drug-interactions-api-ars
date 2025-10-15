package com.example.druginteractions.domain.ports;

import com.example.druginteractions.domain.model.OpenFdaSignal;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public interface OpenFdaClient {
  Mono<OpenFdaSignal> fetchSignals(String drugA, String drugB, int limit);
}
