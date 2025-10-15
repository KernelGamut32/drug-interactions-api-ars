package com.example.druginteractions.adapters.openfda.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class OpenFdaConfig {
  private static final String BASE_URL = "https://api.fda.gov";
  private static final int TIMEOUT_SECONDS = 10;

  @Bean
  public WebClient openFdaWebClient(
      @Value("${openfda.apiKey:}") String apiKey,
      @Value("${openfda.timeout:10}") int timeoutSeconds) {

    HttpClient httpClient =
        HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutSeconds * 1000)
            .doOnConnected(
                conn ->
                    conn.addHandlerLast(new ReadTimeoutHandler(timeoutSeconds, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(timeoutSeconds, TimeUnit.SECONDS)));

    return WebClient.builder()
        .baseUrl(BASE_URL)
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .filter(rateLimitRetryFilter())
        .defaultHeader("apiKey", apiKey)
        .build();
  }

  private ExchangeFilterFunction rateLimitRetryFilter() {
    return ExchangeFilterFunction.ofRequestProcessor(
        clientRequest -> {
          return Mono.just(clientRequest)
              .delayElement(Duration.ofMillis(100)); // Basic rate limiting
        });
  }
}
