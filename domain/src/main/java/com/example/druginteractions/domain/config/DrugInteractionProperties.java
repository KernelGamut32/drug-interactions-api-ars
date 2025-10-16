package com.example.druginteractions.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "drug-interaction")
public class DrugInteractionProperties {
  private int defaultSignalLimit = 10;
}
