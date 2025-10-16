package com.example.druginteractions.adapters.openfda.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

@Configuration
public class OpenFdaConfig {
  private static final Logger log = LoggerFactory.getLogger(OpenFdaConfig.class);

  @Bean
  public WebClient openFdaWebClient(OpenFdaClientProperties properties) {
    HttpClient httpClient =
        HttpClient.create()
            .option(
                ChannelOption.CONNECT_TIMEOUT_MILLIS,
                (int) properties.getConnectTimeout().toMillis())
            .doOnConnected(
                conn ->
                    conn.addHandlerLast(
                            new ReadTimeoutHandler(
                                properties.getReadTimeout().toMillis(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(
                            new WriteTimeoutHandler(
                                properties.getReadTimeout().toMillis(), TimeUnit.MILLISECONDS)));

    return WebClient.builder()
        .baseUrl(properties.getBaseUrl())
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .filter(
            (request, next) -> {
              if (request.method() == HttpMethod.GET) {
                return next.exchange(request)
                    .retryWhen(
                        Retry.backoff(
                                properties.getRetry().getMaxAttempts(),
                                properties.getRetry().getInitialInterval())
                            .maxBackoff(properties.getRetry().getMaxInterval())
                            .jitter(0.75)
                            .filter(
                                throwable -> {
                                  boolean shouldRetry = shouldRetryException(throwable);
                                  if (!shouldRetry) {
                                    log.warn(
                                        "Not retrying request due to non-retryable exception: {}",
                                        throwable.getMessage());
                                  }
                                  return shouldRetry;
                                })
                            .doBeforeRetry(
                                retrySignal ->
                                    log.info(
                                        "Retrying request attempt {} after failure: {}",
                                        retrySignal.totalRetries() + 1,
                                        retrySignal.failure().getMessage())));
              }
              return next.exchange(request);
            })
        .build();
  }

  private boolean shouldRetryException(Throwable throwable) {
    // Don't retry client exceptions (4xx)
    if (throwable instanceof IllegalArgumentException) {
      return false;
    }

    // Add more specific exception handling here if needed
    // For example, you might want to retry on connection timeouts
    // but not on authentication failures

    return true;
  }
}
