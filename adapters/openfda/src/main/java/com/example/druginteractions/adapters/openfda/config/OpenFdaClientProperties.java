package com.example.druginteractions.adapters.openfda.config;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "openfda.client")
public class OpenFdaClientProperties {
  private String baseUrl = "https://api.fda.gov";
  private Duration connectTimeout = Duration.ofSeconds(5);
  private Duration readTimeout = Duration.ofSeconds(10);
  private Retry retry = new Retry();

  @Data
  public static class Retry {
    private int maxAttempts = 3;
    private Duration initialInterval = Duration.ofMillis(100);
    private Duration maxInterval = Duration.ofSeconds(1);
    private double multiplier = 2.0;
  }
}
